package com.demo.network.okhttp;

import com.demo.network.INetworkRequestInfo;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * @author 尉迟涛
 * create time : 2020/3/5 10:56
 * description :
 */
public class OkHttpInstance {

    private static final String ROOT_CA_CERT =
            "-----BEGIN CERTIFICATE-----\n" +
                    "MIIDTTCCAjUCFEkNsGijEu+unLZr/qsATjcpYtfBMA0GCSqGSIb3DQEBCwUAMGMx\n" +
                    "CzAJBgNVBAYTAkNOMRIwEAYDVQQIDAlHdWFuZ2RvbmcxEjAQBgNVBAcMCUd1YW5n\n" +
                    "emhvdTESMBAGA1UECgwJV2VpVGFvLmNvMQswCQYDVQQLDAJJVDELMAkGA1UEAwwC\n" +
                    "V1QwHhcNMjAwNDE4MTYwMjEyWhcNMjMwMTEzMTYwMjEyWjBjMQswCQYDVQQGEwJD\n" +
                    "TjESMBAGA1UECAwJR3Vhbmdkb25nMRIwEAYDVQQHDAlHdWFuZ3pob3UxEjAQBgNV\n" +
                    "BAoMCVdlaVRhby5jbzELMAkGA1UECwwCSVQxCzAJBgNVBAMMAldUMIIBIjANBgkq\n" +
                    "hkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtWrBahhELD37SRSHMLYIOtGryNDZ2Nmw\n" +
                    "t0NxLUJsf31IOSouPkLS9MWPbGDzSdS0ikuyNS0ghuWG1xpJQON+xmKfqtAa3Hyk\n" +
                    "G/Nxj5BMiU2IZL6ta9g5L3K4v09r887cUosQNIg1vDipUTR/vcf6Fvm2OkeKll6I\n" +
                    "du2+sdH0X+6Ainpfd2HpSzKisYdwSIEJWwv/iBybSo8gwh2Z3ocTfRXCkvIohAPi\n" +
                    "lSUVAsw19LqU4fFEQr0+Fmqqk/uHshbiyG2NMjUZmm4YoXrIyMH0FnfwXjTMnuo4\n" +
                    "kBc3nYk0euTv/u83/v3LuE2iqpFKogWKdUaMalemtu/C/LnOxK/A/QIDAQABMA0G\n" +
                    "CSqGSIb3DQEBCwUAA4IBAQCilvzuNmdXmk1P45u/ECQ86Z7YwEz+r8xq9eerO2B+\n" +
                    "3ERMchjcgEmWrwuAvlO8i9Xr10JvqG1ay66978vh1WjWQ8K3n8WHIEJRtd4w25Kd\n" +
                    "5mRH/fS5vnYayFH5VypJrMnr8BN3d+uFgZzYN4TV4HP3iB9pje6ezK1LyOtOyEGo\n" +
                    "Q2mp0974aQPt866T5/scxUHhq7STtCo+XsOElDlF9QBvCSNPcQh8i/+SSI9Vfp1Z\n" +
                    "55UmHzzk6j8asg/1JOril3UTSYx/Ut4NqQCiqBkCvQhwj8j4DMQ7ZPAVhrQyiebN\n" +
                    "SrUeSyCFl1hzODGrgsBrTcilIokRUENEbB+g+opW1yfa\n" +
                    "-----END CERTIFICATE-----";


    private static INetworkRequestInfo networkRequestInfo;
    private static RequestInterceptor sHttpsRequestInterceptor;
    private static ResponseInterceptor sHttpsResponseInterceptor;

    private static class Instance {
        private static OkHttpInstance instance = new OkHttpInstance();
    }

    public static void setNetworkRequestInfo(INetworkRequestInfo requestInfo) {
        networkRequestInfo = requestInfo;
        sHttpsRequestInterceptor = new RequestInterceptor(requestInfo);
        sHttpsResponseInterceptor = new ResponseInterceptor();
    }

    public static OkHttpClient get() {
        return Instance.instance.client;
    }


    private OkHttpClient client;

    private OkHttpInstance() {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS);

            // 可以统一添加网络参数到请求头
            builder.addInterceptor(sHttpsRequestInterceptor);
            // 网络请求返回的时候的数据处理
            builder.addInterceptor(sHttpsResponseInterceptor);
            // log
            addLogInterceptor(builder);

            //TrustManager
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(getKeyStore());

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }

            //SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
            builder.sslSocketFactory(
                    sslContext.getSocketFactory(),
                    (X509TrustManager) trustManagers[0]
            );
            builder.hostnameVerifier(new SSLHostnameVerifier());

            client = builder.build();
            client.dispatcher().setMaxRequestsPerHost(20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加log拦截器，BODY打印信息，NONE不打印信息
     */
    private void addLogInterceptor(OkHttpClient.Builder builder) {
        LoggingInterceptor logging = new LoggingInterceptor();
        logging.setLevel(networkRequestInfo.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(logging);
    }

    /**
     * 导入证书
     */
    private KeyStore getKeyStore() {
        // 添加https证书
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);

            // 从文件中获取
            //InputStream is = BaseApp.app.getAssets().open("rootCA.crt");
            // 从代码中获取
            InputStream is = new Buffer().writeUtf8(ROOT_CA_CERT).inputStream();
            // 签名文件设置证书
            keyStore.setCertificateEntry("0", certificateFactory.generateCertificate(is));
            is.close();
            return keyStore;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class SSLHostnameVerifier implements HostnameVerifier {

//        private static final String HOSTNAME = "122.51.102.227";

        @Override
        public boolean verify(String hostname, SSLSession session) {
//            return HOSTNAME.equals(hostname) && HOSTNAME.equals(session.getPeerHost());
            return true;
        }
    }

}
