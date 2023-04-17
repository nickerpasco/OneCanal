package pe.com.onecanal.framework.hilt.modules

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.RoundingMode


/**
 * Created by Sergio Carrillo Diestra on 20/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class DoubleGsonAdapter : JsonSerializer<Double> {
    override fun serialize(
        src: Double?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val value = BigDecimal(src!!).setScale(0, RoundingMode.HALF_UP)
        return JsonPrimitive(value)
    }
}
