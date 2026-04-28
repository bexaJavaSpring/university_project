package uz.java.spring_boot_application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import uz.java.spring_boot_application.client.KeycloakServiceClient;
import uz.java.spring_boot_application.config.CustomAuthenticationProvider;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.auth.LoginResponse;
import uz.java.spring_boot_application.dto.user.UserResponse;
import uz.java.spring_boot_application.entities.User;
import uz.java.spring_boot_application.exception.BadRequestException;
import uz.java.spring_boot_application.exception.GenericRuntimeException;
import uz.java.spring_boot_application.mapper.UserMapper;
import uz.java.spring_boot_application.security.CustomUserDetails;

import java.io.IOException;

@Service
@Slf4j
public class AuthService {

    private final CustomAuthenticationProvider authenticationProvider;
    private final KeycloakServiceClient keycloakServiceClient;
    private final UserSession userSession;
    private final UserMapper userMapper;

    @Value("${app.keycloak.client-id}")
    private String clientId;

    @Value("${app.keycloak.client-secret}")
    private String clientSecret;

    public AuthService(@Value("${app.keycloak.keycloak-server-url}") String baseUrl,
                       CustomAuthenticationProvider authenticationProvider, UserSession userSession, UserMapper userMapper) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.keycloakServiceClient = retrofit.create(KeycloakServiceClient.class);
        this.authenticationProvider = authenticationProvider;
        this.userSession = userSession;
        this.userMapper = userMapper;
    }

    public UserResponse me() {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        return userMapper.toResponse(user);
    }

    public LoginResponse login(String username, String password) {
        try {
            Response<LoginResponse> response = keycloakServiceClient.getToken(
                    "password",
                    clientId,
                    clientSecret,
                    username,
                    password
            ).execute();
            String errorBody = "";
            if (!response.isSuccessful()) {
                try {
                    if (response.errorBody() != null) {
                        errorBody = response.errorBody().string();
                        throw new BadRequestException("Token olishda xatolik: " + errorBody);
                    }
                } catch (IOException e) {
                    throw new BadRequestException("Xatolikni o'qishda muammo yuz berdi.");
                }
            }
            if (response.body() == null) {
                throw new BadRequestException("Token olishda xatolik : response.body() == null" + "\n" + response.message() + "\n" + " --- getPinfl");
            }
            if (response.body().getAccessToken() == null) {
                throw new BadRequestException("Token olishda xatolik : response.body().getAccessToken() == null" + "\n" + response.message() + "\n" + " --- getPinfl");
            }
            DecodedJWT data = JWT.decode(response.body().getAccessToken());

            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                    data.getClaim("preferred_username").asString(), password
            ));
            return LoginResponse.builder()
                    .accessToken(response.body().getAccessToken())
                    .refreshToken(response.body().getRefreshToken())
                    .build();
        } catch (IOException e) {
            throw new GenericRuntimeException("Token olishda xatolik: " + e.getMessage());
        }
    }
}
