package com.fooddelivery.FoodHouse.service;


import com.fooddelivery.FoodHouse.Repository.FoodRespository;
import com.fooddelivery.FoodHouse.entity.FoodEntity;
import com.fooddelivery.FoodHouse.io.FoodRequest;
import com.fooddelivery.FoodHouse.io.FoodResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.SdkGlobalTime;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService{

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket_name;



    private final FoodRespository foodRespository;

    @Override
    public String uploadFile(MultipartFile file) {
        String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String key = UUID.randomUUID().toString()+"."+filenameExtension;
        try{
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket_name)
                    .key(key).acl("public-read")
                    .contentType(file.getContentType()).build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if(response.sdkHttpResponse().isSuccessful()){
                return "https://"+bucket_name+".s3.amazonaws.com/"+key; //returning own explicitly uploaded iamge url
            }else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"File could not upload successfully");
            }
        }catch (IOException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error occurred ");
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
      FoodEntity newFoodEntity = convertToEntity(request);

       String imageUrl =  uploadFile(file);

        newFoodEntity.setImageUrl(imageUrl);

        newFoodEntity = foodRespository.save(newFoodEntity);

        System.out.println("Food Saved:" +newFoodEntity.getId());

        return convertToResponse(newFoodEntity);


    }

    @Override
    public List<FoodResponse> readFoods() {
       List<FoodEntity> databaseEntries =  foodRespository.findAll();

        //convert  List<FoodEntity> to List<FoodResponse> using java 8
       return databaseEntries.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());

    }

    @Override
    public FoodResponse readFood(String id) {
        FoodEntity existingFood = foodRespository.findById(id).orElseThrow(()-> new RuntimeException("Food not Foound"));

        return convertToResponse(existingFood);
    }


    //deleting from s3 bucket
    @Override
    public Boolean deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket_name)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }


    //delete from Mongoddb
    @Override
    public void deleteFood(String id) {
         FoodResponse foodResponse = readFood(id);

        String imageUrl = foodResponse.getImageUrl();

        String fileName =  imageUrl.substring(imageUrl.lastIndexOf("/")+1);

         boolean isFileDeleted = deleteFile(fileName);

         //if file is deleted from s3 bucket then delete from mongoDb

        if(isFileDeleted) {
            foodRespository.deleteById(foodResponse.getId());
        }
    }

    //method converts the foodRequest to FoodEntity

    private FoodEntity convertToEntity(FoodRequest request){
          return FoodEntity.builder()
                  .name(request.getName())
                  .description(request.getDescription())
                  .category(request.getCategory())
                  .price(request.getPrice()).build();

    }

    private FoodResponse convertToResponse(FoodEntity entity){
        return FoodResponse.builder()
                .name(entity.getName())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .id(entity.getId())
                .imageUrl(entity.getImageUrl()).build();

    }
}
