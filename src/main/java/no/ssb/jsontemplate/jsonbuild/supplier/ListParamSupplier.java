package no.ssb.jsontemplate.jsonbuild.supplier;

import java.security.SecureRandom;
import java.util.List;
import java.util.function.Supplier;

public class ListParamSupplier<T> implements Supplier<T> {
    private static final SecureRandom random = new SecureRandom();

    private List<T> listParam;

    public ListParamSupplier(List<T> listParam) {
        this.listParam = listParam;
    }

    public List<T> getListParam() {
        return listParam;
    }

    @Override
    public T get() {
        return listParam.get(random.nextInt(listParam.size()));
    }
}
