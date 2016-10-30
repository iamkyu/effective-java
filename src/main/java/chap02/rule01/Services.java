package chap02.rule01;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kj Nam
 * @since 2016-10-30
 */
public class Services {
    interface Service {
        //서비스인터페이스. 서비스 고유 메서드 포함
    }

    interface Provider {
        //서비스 제공자 인터페이스
        Service newService();
    }


    private Services() {} // 객체생성방지

    //서비스이름과 서비스간 대응관계 보관
    private static final Map<String, Provider> providers = new ConcurrentHashMap<>();
    public static final String DEFAULT_PROVIDER_NAME = "<def>";

    //제공자등록 API
    public static void registerDefaultProvider(Provider p) {
        registerProvider(DEFAULT_PROVIDER_NAME, p);
    }
    private static void registerProvider(String name, Provider p) {
        providers.put(name, p);
    }

    //서비스접근 API
    public static Service newInstance() {
        return newInstance(DEFAULT_PROVIDER_NAME);
    }
    public static Service newInstance(String name) {
        Provider p = providers.get(name);
        if (p == null) {
            throw new IllegalArgumentException("No provider registered with name: " + name);
        }

        return p.newService();
    }
}
