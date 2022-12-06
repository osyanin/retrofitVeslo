package client;

import client.transport.AuthAction;
import client.transport.querymap.LoginUserQueryMap;
import io.qameta.allure.Description;
import model.Status;
import model.User;
import retrofit2.http.*;
import veslo.AResponse;

public interface UserApi {
    @POST("user/auth/login")
    @Description("Logs user into the system")
    AResponse<Status, Status> loginUser(@Query("username") Object username, @Query("password") Object password);

    @POST("user/auth/login")
    @Description("Logs user into the system")
    AResponse<Status, Status> loginUser(@QueryMap() LoginUserQueryMap queryMap);

    default void authenticateUser(LoginUserQueryMap namePass) {
        final Status status = loginUser(namePass)
                .assertResponse(a -> a.assertHttpStatusCodeIs(200).assertSucBodyNotNull()).getSucDTO();
        // String token = status.getToken();
        // For this sample, uses the api key 'special-key'
        String token = "special-key";
        AuthAction.setToken(token);
    }

    @GET("user/profile/get_user_data")
    @Description("Get user by user name")
    AResponse<User, Status> getUserByName(@Path("username") Object username);

    @GET("user/auth/logout")
    @Description("Logs out current logged in user session")
    AResponse<Status, Status> logoutUser();

    default void logout() {
        logoutUser();
        AuthAction.removeToken();
    }

}
