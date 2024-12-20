package usst.spm.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

/**
 * @author jyzxc
 * @since 2024-12-3
 */
@Service
public class MinioService {

    @Resource
    private MinioClient minioClient;
    // 替换为你的桶名
    private static final String BUCKET_NAME = "spm";

    /**
     * 上传文件到 MinIO
     *
     * @param objectName 文件名
     * @param inputStream 文件流
     * @param contentType 文件类型（如 "image/png"）
     * @throws Exception 如果发生错误
     */
    public void uploadFile(String objectName, InputStream inputStream, String contentType) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .build()
        );
    }

    /**
     * 获取文件 URL
     *
     * @param objectName 文件名
     * @return 文件 URL
     * @throws Exception 如果发生错误
     */
    public String getFileUrl(String objectName) throws Exception {
        String URI = minioClient.getPresignedObjectUrl(
                io.minio.GetPresignedObjectUrlArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(objectName)
                        .method(io.minio.http.Method.GET)
                        .build());
        return URI.split("\\?")[0];
    }

    /**
     * 删除文件
     *
     * @param objectName 文件名
     * @throws Exception 如果发生错误
     */
    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(objectName)
                        .build()
        );
    }


    public String setPictureUri(String base64Pic, String directory) throws Exception {
        if (base64Pic.startsWith("data:image")) {
            // 1. 解析 Base64 数据
            String[] parts = base64Pic.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid Base64 format");
            }
            // 获取 Base64 数据部分
            String base64Data = parts[1];

            // 2. 解码 Base64 数据
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

            // 3. 自定义文件名
            String objectName = directory + "/" + UUID.randomUUID() + ".png";

            // 4. 上传图片到 MinIO
            try (InputStream inputStream = new ByteArrayInputStream(decodedBytes)) {
                this.uploadFile(objectName, inputStream, "image/png");
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image to MinIO", e);
            }

            // 5. 获取图片 URL

            // 6. 更新 pic 字段
            return this.getFileUrl(objectName);
        }
        return null;
    }
}
