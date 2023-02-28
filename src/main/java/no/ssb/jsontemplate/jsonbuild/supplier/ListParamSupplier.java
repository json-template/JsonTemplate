package no.ssb.jsontemplate.jsonbuild.supplier;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class ListParamSupplier<T> implements Supplier<T> {

    private List<T> listParam;

    public ListParamSupplier(List<T> listParam) {
        this.listParam = listParam;
    }

    public List<T> getListParam() {
        return listParam;
    }

    @Override
    public T get() {
        return listParam.get(ThreadLocalRandom.current().nextInt(listParam.size()));
    }
}
