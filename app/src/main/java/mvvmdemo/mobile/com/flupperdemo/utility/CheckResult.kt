package mvvmdemo.mobile.com.flupperdemo.utility

import mvvmdemo.mobile.com.flupperdemo.model.Product

class CheckResult {

    companion object
    {
        @JvmStatic
        fun checkBlankResultData(instranse: Any):Boolean {
            if (instranse!=null && instranse::class == Product::class) {
                val p = instranse as Product;
                return !p?.name?.isEmpty()
                        && !p?.desc?.isEmpty()
                        && !p?.reguler_price?.isEmpty()
                        && !p.sale_price?.isEmpty();
            }
            return false;
        }


        @JvmStatic
        fun checkDataValidation(product: Product):String {

            if(product.name.trim().isEmpty()) return "Enter Product name"
            else if(product.desc.trim().isEmpty()) return "Enter Product description"
            else if(product.reguler_price.trim().isEmpty()) return "Enter Product base price"
            else if(product.sale_price.trim().isEmpty())  return "Enter Product discounted price"
            else if(product.sale_price.toFloat()>product.reguler_price.toFloat()) return "Discounted price should not be greater than reguler price."
            else if(product.colors.size==0) return "Must choose at least one color"
            else if(product.stores.size==0) return "Must enter at least one store name"
            return ""
        }


    }


}