package uz.java.spring_boot_application.client;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import uz.java.spring_boot_application.dto.auth.LoginResponse;

public interface KeycloakServiceClient {
    @FormUrlEncoded
    @POST("/realms/University-app/protocol/openid-connect/token")
    Call<LoginResponse> getToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("username") String username,
            @Field("password") String password
    );
}
