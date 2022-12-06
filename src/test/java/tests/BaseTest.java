package tests;

import client.UserApi;
import client.transport.BankInterceptor;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import retrofit2.Retrofit;
import veslo.AllureCallAdapterFactory;
import veslo.JacksonConverterFactory;

import static veslo.client.TrustSocketHelper.*;

public abstract class BaseTest {
    private static final String URL = System.getProperty("service_url", "");

    protected static final UserApi USER_API = createJacksonClient(UserApi.class);

    private static <CLIENT> CLIENT createJacksonClient(final Class<CLIENT> clientClass) {
        return new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .followRedirects(true)
                        .followSslRedirects(true)
                        .hostnameVerifier(TRUST_ALL_HOSTNAME)
                        .sslSocketFactory(TRUST_ALL_SSL_SOCKET_FACTORY, TRUST_ALL_CERTS_MANAGER)
                        .addNetworkInterceptor(new BankInterceptor())
                        .build())
                .baseUrl(URL)
                .addCallAdapterFactory(new AllureCallAdapterFactory())
//                .addCallAdapterFactory(new AllureCallAdapterFactory(ExampleCustomResponse::new))
                .addConverterFactory(new JacksonConverterFactory())
//                .addConverterFactory(new CustomJacksonConverterFactory())
                .build()
                .create(clientClass);
    }

    /*@BeforeEach
    public void logout() {
        USER_API.logout();
    }*/
}
