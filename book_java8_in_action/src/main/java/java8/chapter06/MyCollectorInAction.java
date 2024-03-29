package java8.chapter06;

import java8.BaseAction;
import java8.chapter05.Dish;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件描述：
 *
 * @author Bo.Zhao
 * @version 3.0
 * @since 18/6/20
 */
public class MyCollectorInAction extends BaseAction {

    @Test
    public void testAll() {
        toListCollector();
    }

    private void toListCollector() {
        List<String> collect = mockMenu().stream().map(Dish::getName).collect(
                ArrayList::new, List::add, List::addAll
        );
        show(collect);
    }
}
