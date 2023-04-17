package pe.com.onecanal.domain.entity

import kotlin.collections.ArrayList

/**
 * Created by Sergio Carrillo Diestra on 2/02/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class FeeDomain(val feeRanges: FeeType<Array<FeeItem>>, val igv: FeeType<Double>, val itf: FeeType<Double>)