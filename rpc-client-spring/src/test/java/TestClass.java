import com.better.annotations.RpcReference;
import com.better.discovery.impl.ZookeeperDiscovery;
import com.better.test.Fooo;

public class TestClass {

    @RpcReference
    private ZookeeperDiscovery zookeeperDiscovery;

    @RpcReference
    private Fooo fooo;

    @RpcReference
    private Foo foo;

}
