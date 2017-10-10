package similarity.linzijun.com.converter.orm.converter;


import com.linzijun.similarity.converter.annotation.ConverterMethod;

import java.util.Date;

/**
 * Created by linzijun on
 */

public class DateConverter {
    @ConverterMethod
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @ConverterMethod
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
