import com.pinet.PinetApplication;
import com.pinet.rest.entity.*;
import com.pinet.rest.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = PinetApplication.class)
public class ProductTest {

    @Autowired
    private IShopProductService shopProductService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductTypeService productTypeService;

    @Autowired
    private IProductSkuService productSkuService;

    @Autowired
    private IProductSpecService productSpecService;

    @Autowired
    private IShopProductSpecService shopProductSpecService;


    @Test
    public void test(){
        ProductType productType = new ProductType();
        productType.setCreateTime(new Date());
        productType.setCreateBy(1L);
        productType.setTypeState(0);
        productType.setTypeName("饮品");
        productType.setId(1L);
        productTypeService.save(productType);

        Product product = new Product();
        product.setId(1L);
        product.setCreateTime(new Date());
        product.setProductImg("");
        product.setProductDesc("");
        product.setProductName("奶茶");
        product.setProductTypeId(productType.getId());
        product.setProductType(productType.getTypeName());
        product.setCreateBy(1L);
        product.setCreateUser("admin");
        productService.save(product);

        List<ProductSku> skus = new ArrayList<>();
        ProductSku sku = new ProductSku();
        sku.setCreateTime(new Date());
        sku.setCreateBy(1L);
        sku.setSkuName("规格1");
        sku.setId(1L);

        ProductSku sku2 = new ProductSku();
        sku2.setCreateTime(new Date());
        sku2.setCreateBy(1L);
        sku2.setSkuName("精度");
        sku2.setId(2L);

        ProductSku sku3 = new ProductSku();
        sku3.setCreateTime(new Date());
        sku3.setCreateBy(1L);
        sku3.setSkuName("冰度");
        sku3.setId(3L);
        skus.add(sku);
        skus.add(sku2);
        skus.add(sku3);
        productSkuService.saveBatch(skus);

        //商品规格1
        ProductSpec productSpec = new ProductSpec();
        productSpec.setSkuId(sku.getId());
        productSpec.setProductId(product.getId());
        productSpec.setSpecName("大杯");
        productSpec.setMarketPrice(new BigDecimal("12.5"));
        //商品规格2
        ProductSpec productSpec2 = new ProductSpec();
        productSpec2.setSkuId(sku.getId());
        productSpec2.setProductId(product.getId());
        productSpec2.setSpecName("中杯");
        productSpec2.setMarketPrice(BigDecimal.TEN);

        //商品规格3
        ProductSpec productSpec3 = new ProductSpec();
        productSpec3.setSkuId(sku.getId());
        productSpec3.setProductId(product.getId());
        productSpec3.setSpecName("小杯");
        productSpec3.setMarketPrice(new BigDecimal(8));

        //规格2
        ProductSpec productSpec4 = new ProductSpec();
        productSpec4.setSkuId(sku2.getId());
        productSpec4.setProductId(product.getId());
        productSpec4.setSpecName("无糖");
        productSpec4.setMarketPrice(BigDecimal.ZERO);

        ProductSpec productSpec5 = new ProductSpec();
        productSpec5.setSkuId(sku2.getId());
        productSpec5.setProductId(product.getId());
        productSpec5.setSpecName("五分糖");
        productSpec5.setMarketPrice(BigDecimal.ZERO);

        ProductSpec productSpec6 = new ProductSpec();
        productSpec6.setSkuId(sku2.getId());
        productSpec6.setProductId(product.getId());
        productSpec6.setSpecName("七分糖");
        productSpec6.setMarketPrice(BigDecimal.ZERO);

        //规格3
        ProductSpec productSpec7 = new ProductSpec();
        productSpec7.setSkuId(sku3.getId());
        productSpec7.setSkuId(sku3.getId());
        productSpec7.setProductId(product.getId());
        productSpec7.setSpecName("热饮");
        productSpec7.setMarketPrice(BigDecimal.ZERO);

        ProductSpec productSpec8 = new ProductSpec();
        productSpec8.setSkuId(sku3.getId());
        productSpec8.setSkuId(sku3.getId());
        productSpec8.setProductId(product.getId());
        productSpec8.setSpecName("长温");
        productSpec8.setMarketPrice(BigDecimal.ZERO);

        ProductSpec productSpec9 = new ProductSpec();
        productSpec9.setSkuId(sku3.getId());
        productSpec9.setSkuId(sku3.getId());
        productSpec9.setProductId(product.getId());
        productSpec9.setSpecName("少冰");
        productSpec9.setMarketPrice(BigDecimal.ZERO);

        List<ProductSpec> specList = new ArrayList<>();
        specList.add(productSpec);
        specList.add(productSpec2);
        specList.add(productSpec3);
        specList.add(productSpec4);
        specList.add(productSpec5);
        specList.add(productSpec6);
        specList.add(productSpec7);
        specList.add(productSpec8);
        specList.add(productSpec9);
        productSpecService.saveBatch(specList);

        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setShopId(1L);
        shopProduct.setCreateBy(1L);
        shopProduct.setProductDesc(product.getProductDesc());
        shopProduct.setProdId(product.getId());
        shopProduct.setProductImg(product.getProductImg());
        shopProduct.setProductType(product.getProductType());
        shopProduct.setProductTypeId(product.getProductTypeId());
        shopProduct.setShopProdStatus(1);
        shopProduct.setCreateTime(new Date());
        shopProductService.save(shopProduct);

//        //规格1
//        ShopProductSpec shopProductSpec = new ShopProductSpec();
//        shopProductSpec.setPrice(new BigDecimal(0));
//        shopProductSpec.setProdSpecId(productSpec.getId());
//        shopProductSpec.setShopProdId(shopProduct.getProdId());
//        shopProductSpec.setSkuId(productSpec.getSkuId());
//        shopProductSpec.setSpecName(productSpec.getSpecName());
//        shopProductSpec.setStock(200);
//        shopProductSpec.setCreateTime(new Date());
//        shopProductSpec.setCreateBy(1L);
//
//        //规格2
//        ShopProductSpec shopProductSpec2 = new ShopProductSpec();
//        shopProductSpec2.setPrice(new BigDecimal(0));
//        shopProductSpec2.setProdSpecId(2L);
//        shopProductSpec2.setShopProdId(shopProduct.getProdId());
//        shopProductSpec2.setSkuId(productSpec2.getSkuId());
//        shopProductSpec2.setSpecName(productSpec2.getSpecName());
//        shopProductSpec2.setStock(200);
//        shopProductSpec2.setCreateTime(new Date());
//        shopProductSpec2.setCreateBy(1L);
//
//        //规格3
//        ShopProductSpec shopProductSpe3 = new ShopProductSpec();
//        shopProductSpe3.setPrice(new BigDecimal(0));
//        shopProductSpe3.setProdSpecId(3L);
//        shopProductSpe3.setShopProdId(shopProduct.getProdId());
//        shopProductSpe3.setSkuId(productSpec3.getSkuId());
//        shopProductSpe3.setSpecName(productSpec3.getSpecName());
//        shopProductSpe3.setStock(200);
//        shopProductSpe3.setCreateTime(new Date());
//        shopProductSpe3.setCreateBy(1L);


        ShopProductSpec shopProductSpe4= new ShopProductSpec();
        shopProductSpe4.setPrice(new BigDecimal(0));
        shopProductSpe4.setProdSpecId(4L);
        shopProductSpe4.setShopProdId(shopProduct.getProdId());
        shopProductSpe4.setSkuId(productSpec4.getSkuId());
        shopProductSpe4.setSpecName(productSpec4.getSpecName());
        shopProductSpe4.setStock(200);
        shopProductSpe4.setCreateTime(new Date());
        shopProductSpe4.setCreateBy(1L);

        ShopProductSpec shopProductSpe5= new ShopProductSpec();
        shopProductSpe5.setPrice(new BigDecimal(0));
        shopProductSpe5.setProdSpecId(5L);
        shopProductSpe5.setShopProdId(shopProduct.getProdId());
        shopProductSpe5.setSkuId(productSpec5.getSkuId());
        shopProductSpe5.setSpecName(productSpec5.getSpecName());
        shopProductSpe5.setStock(200);
        shopProductSpe5.setCreateTime(new Date());
        shopProductSpe5.setCreateBy(1L);

        ShopProductSpec shopProductSpe6= new ShopProductSpec();
        shopProductSpe6.setPrice(new BigDecimal(0));
        shopProductSpe6.setProdSpecId(6L);
        shopProductSpe6.setShopProdId(shopProduct.getProdId());
        shopProductSpe6.setSkuId(productSpec6.getSkuId());
        shopProductSpe6.setSpecName(productSpec6.getSpecName());
        shopProductSpe6.setStock(200);
        shopProductSpe6.setCreateTime(new Date());
        shopProductSpe6.setCreateBy(1L);

        ShopProductSpec shopProductSpe7= new ShopProductSpec();
        shopProductSpe7.setPrice(new BigDecimal(0));
        shopProductSpe7.setProdSpecId(7L);
        shopProductSpe7.setShopProdId(shopProduct.getProdId());
        shopProductSpe7.setSkuId(productSpec7.getSkuId());
        shopProductSpe7.setSpecName(productSpec7.getSpecName());
        shopProductSpe7.setStock(200);
        shopProductSpe7.setCreateTime(new Date());
        shopProductSpe7.setCreateBy(1L);

        ShopProductSpec shopProductSpe8= new ShopProductSpec();
        shopProductSpe8.setPrice(new BigDecimal(0));
        shopProductSpe8.setProdSpecId(8L);
        shopProductSpe8.setShopProdId(shopProduct.getProdId());
        shopProductSpe8.setSkuId(productSpec8.getSkuId());
        shopProductSpe8.setSpecName(productSpec8.getSpecName());
        shopProductSpe8.setStock(200);
        shopProductSpe8.setCreateTime(new Date());
        shopProductSpe8.setCreateBy(1L);

        ShopProductSpec shopProductSpe9= new ShopProductSpec();
        shopProductSpe9.setPrice(new BigDecimal(0));
        shopProductSpe9.setProdSpecId(9L);
        shopProductSpe9.setShopProdId(shopProduct.getProdId());
        shopProductSpe9.setSkuId(productSpec9.getSkuId());
        shopProductSpe9.setSpecName(productSpec9.getSpecName());
        shopProductSpe9.setStock(200);
        shopProductSpe9.setCreateTime(new Date());
        shopProductSpe9.setCreateBy(1L);

        List<ShopProductSpec> shopProductSpecs = new ArrayList<>();
        shopProductSpecs.add(shopProductSpe4);
        shopProductSpecs.add(shopProductSpe5);
        shopProductSpecs.add(shopProductSpe6);
        shopProductSpecs.add(shopProductSpe7);
        shopProductSpecs.add(shopProductSpe8);
        shopProductSpecs.add(shopProductSpe9);
        shopProductSpecService.saveBatch(shopProductSpecs);
    }


    @Test
    public void test2(){
        ProductType productType = new ProductType();
        productType.setCreateTime(new Date());
        productType.setCreateBy(1L);
        productType.setTypeState(0);
        productType.setTypeName("甜品类");
        productType.setId(2L);
//        productTypeService.save(productType);
//
        Product product = new Product();
        product.setId(2L);
        product.setCreateTime(new Date());
        product.setProductImg("");
        product.setProductDesc("蛋糕 描述");
        product.setProductName("蛋糕");
        product.setProductTypeId(productType.getId());
        product.setProductType(productType.getTypeName());
        product.setCreateBy(1L);
        product.setCreateUser("admin");
        product.setDelFlag(0);
//        productService.save(product);

        List<ProductSku> skus = new ArrayList<>();
        ProductSku sku = new ProductSku();
        sku.setCreateTime(new Date());
        sku.setCreateBy(1L);
        sku.setSkuName("规格1");
        sku.setId(4L);

        ProductSku sku2 = new ProductSku();
        sku2.setCreateTime(new Date());
        sku2.setCreateBy(1L);
        sku2.setSkuName("糖");
        sku2.setId(5L);

        ProductSku sku3 = new ProductSku();
        sku3.setCreateTime(new Date());
        sku3.setCreateBy(1L);
        sku3.setSkuName("夹心");
        sku3.setId(6L);
        skus.add(sku);
        skus.add(sku2);
        skus.add(sku3);
//        productSkuService.saveBatch(skus);

        //商品规格1
        ProductSpec productSpec = new ProductSpec();
        productSpec.setSkuId(sku.getId());
        productSpec.setProductId(product.getId());
        productSpec.setSpecName("5英寸 （动物奶油）（2人分）");
        productSpec.setMarketPrice(new BigDecimal("98"));
        //商品规格2
        ProductSpec productSpec2 = new ProductSpec();
        productSpec2.setSkuId(sku.getId());
        productSpec2.setProductId(product.getId());
        productSpec2.setSpecName("6英寸 （动物奶油）（4人分）");
        productSpec2.setMarketPrice(new BigDecimal("128"));

        //商品规格3
        ProductSpec productSpec3 = new ProductSpec();
        productSpec3.setSkuId(sku.getId());
        productSpec3.setProductId(product.getId());
        productSpec3.setSpecName("8英寸 （牛奶奶油）（6人分）");
        productSpec3.setMarketPrice(new BigDecimal("168"));

        //商品规格4
        ProductSpec productSpec44 = new ProductSpec();
        productSpec44.setSkuId(sku.getId());
        productSpec44.setProductId(product.getId());
        productSpec44.setSpecName("10英寸 （牛奶奶油）（8人分）");
        productSpec44.setMarketPrice(new BigDecimal("298"));

        //规格2
        ProductSpec productSpec4 = new ProductSpec();
        productSpec4.setSkuId(sku2.getId());
        productSpec4.setProductId(product.getId());
        productSpec4.setSpecName("韩国白砂糖");
        productSpec4.setMarketPrice(BigDecimal.ZERO);

        ProductSpec productSpec5 = new ProductSpec();
        productSpec5.setSkuId(sku2.getId());
        productSpec5.setProductId(product.getId());
        productSpec5.setSpecName("韩国幼砂糖");
        productSpec5.setMarketPrice(BigDecimal.ZERO);
//
//        ProductSpec productSpec6 = new ProductSpec();
//        productSpec6.setSkuId(sku2.getId());
//        productSpec6.setProductId(product.getId());
//        productSpec6.setSpecName("七分糖");
//        productSpec6.setMarketPrice(BigDecimal.ZERO);

        //规格3
        ProductSpec productSpec7 = new ProductSpec();
        productSpec7.setSkuId(sku3.getId());
        productSpec7.setSkuId(sku3.getId());
        productSpec7.setProductId(product.getId());
        productSpec7.setSpecName("新鲜芒果");
        productSpec7.setMarketPrice(BigDecimal.ZERO);

        ProductSpec productSpec8 = new ProductSpec();
        productSpec8.setSkuId(sku3.getId());
        productSpec8.setSkuId(sku3.getId());
        productSpec8.setProductId(product.getId());
        productSpec8.setSpecName("越南红心火龙果");
        productSpec8.setMarketPrice(BigDecimal.ZERO);

        ProductSpec productSpec9 = new ProductSpec();
        productSpec9.setSkuId(sku3.getId());
        productSpec9.setSkuId(sku3.getId());
        productSpec9.setProductId(product.getId());
        productSpec9.setSpecName("新西兰猕猴桃");
        productSpec9.setMarketPrice(BigDecimal.ZERO);

        ProductSpec productSpec10 = new ProductSpec();
        productSpec10.setSkuId(sku3.getId());
        productSpec10.setSkuId(sku3.getId());
        productSpec10.setProductId(product.getId());
        productSpec10.setSpecName("菲律宾凤梨");
        productSpec10.setMarketPrice(BigDecimal.ZERO);

        List<ProductSpec> specList = new ArrayList<>();
        specList.add(productSpec);
        specList.add(productSpec2);
        specList.add(productSpec3);
        specList.add(productSpec4);
        specList.add(productSpec5);
        specList.add(productSpec44);
        specList.add(productSpec7);
        specList.add(productSpec8);
        specList.add(productSpec9);
        specList.add(productSpec10);
        productSpecService.saveBatch(specList);

        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setShopId(1L);
        shopProduct.setCreateBy(1L);
        shopProduct.setProductDesc(product.getProductDesc());
        shopProduct.setProdId(product.getId());
        shopProduct.setProductImg(product.getProductImg());
        shopProduct.setProductType(product.getProductType());
        shopProduct.setProductTypeId(product.getProductTypeId());
        shopProduct.setShopProdStatus(1);
        shopProduct.setCreateTime(new Date());
        shopProductService.save(shopProduct);

        //规格1
        ShopProductSpec shopProductSpec = new ShopProductSpec();
        shopProductSpec.setPrice(new BigDecimal(0));
        shopProductSpec.setProdSpecId(productSpec.getId());
        shopProductSpec.setShopProdId(shopProduct.getProdId());
        shopProductSpec.setSkuId(productSpec.getSkuId());
        shopProductSpec.setSpecName(productSpec.getSpecName());
        shopProductSpec.setStock(420);
        shopProductSpec.setCreateTime(new Date());
        shopProductSpec.setCreateBy(1L);

        //规格2
        ShopProductSpec shopProductSpec2 = new ShopProductSpec();
        shopProductSpec2.setPrice(new BigDecimal(0));
        shopProductSpec2.setProdSpecId(productSpec2.getId());
        shopProductSpec2.setShopProdId(shopProduct.getProdId());
        shopProductSpec2.setSkuId(productSpec2.getSkuId());
        shopProductSpec2.setSpecName(productSpec2.getSpecName());
        shopProductSpec2.setStock(273);
        shopProductSpec2.setCreateTime(new Date());
        shopProductSpec2.setCreateBy(1L);

        //规格3
        ShopProductSpec shopProductSpe3 = new ShopProductSpec();
        shopProductSpe3.setPrice(new BigDecimal(0));
        shopProductSpe3.setProdSpecId(productSpec3.getId());
        shopProductSpe3.setShopProdId(shopProduct.getProdId());
        shopProductSpe3.setSkuId(productSpec3.getSkuId());
        shopProductSpe3.setSpecName(productSpec3.getSpecName());
        shopProductSpe3.setStock(186);
        shopProductSpe3.setCreateTime(new Date());
        shopProductSpe3.setCreateBy(1L);

        ShopProductSpec shopProductSpe44 = new ShopProductSpec();
        shopProductSpe44.setPrice(new BigDecimal(0));
        shopProductSpe44.setProdSpecId(productSpec44.getId());
        shopProductSpe44.setShopProdId(shopProduct.getProdId());
        shopProductSpe44.setSkuId(productSpec44.getSkuId());
        shopProductSpe44.setSpecName(productSpec44.getSpecName());
        shopProductSpe44.setStock(190);
        shopProductSpe44.setCreateTime(new Date());
        shopProductSpe44.setCreateBy(1L);


        ShopProductSpec shopProductSpe4= new ShopProductSpec();
        shopProductSpe4.setPrice(new BigDecimal(0));
        shopProductSpe4.setProdSpecId(productSpec4.getId());
        shopProductSpe4.setShopProdId(shopProduct.getProdId());
        shopProductSpe4.setSkuId(productSpec4.getSkuId());
        shopProductSpe4.setSpecName(productSpec4.getSpecName());
        shopProductSpe4.setStock(270);
        shopProductSpe4.setCreateTime(new Date());
        shopProductSpe4.setCreateBy(1L);

        ShopProductSpec shopProductSpe5= new ShopProductSpec();
        shopProductSpe5.setPrice(new BigDecimal(0));
        shopProductSpe5.setProdSpecId(productSpec5.getId());
        shopProductSpe5.setShopProdId(shopProduct.getProdId());
        shopProductSpe5.setSkuId(productSpec5.getSkuId());
        shopProductSpe5.setSpecName(productSpec5.getSpecName());
        shopProductSpe5.setStock(390);
        shopProductSpe5.setCreateTime(new Date());
        shopProductSpe5.setCreateBy(1L);

//        ShopProductSpec shopProductSpe6= new ShopProductSpec();
//        shopProductSpe6.setPrice(new BigDecimal(0));
//        shopProductSpe6.setProdSpecId(productSpec44.getId());
//        shopProductSpe6.setShopProdId(shopProduct.getProdId());
//        shopProductSpe6.setSkuId(productSpec44.getSkuId());
//        shopProductSpe6.setSpecName(productSpec44.getSpecName());
//        shopProductSpe6.setStock(200);
//        shopProductSpe6.setCreateTime(new Date());
//        shopProductSpe6.setCreateBy(1L);

        ShopProductSpec shopProductSpe7= new ShopProductSpec();
        shopProductSpe7.setPrice(new BigDecimal(0));
        shopProductSpe7.setProdSpecId(productSpec7.getId());
        shopProductSpe7.setShopProdId(shopProduct.getProdId());
        shopProductSpe7.setSkuId(productSpec7.getSkuId());
        shopProductSpe7.setSpecName(productSpec7.getSpecName());
        shopProductSpe7.setStock(340);
        shopProductSpe7.setCreateTime(new Date());
        shopProductSpe7.setCreateBy(1L);

        ShopProductSpec shopProductSpe8= new ShopProductSpec();
        shopProductSpe8.setPrice(new BigDecimal(0));
        shopProductSpe8.setProdSpecId(productSpec8.getId());
        shopProductSpe8.setShopProdId(shopProduct.getProdId());
        shopProductSpe8.setSkuId(productSpec8.getSkuId());
        shopProductSpe8.setSpecName(productSpec8.getSpecName());
        shopProductSpe8.setStock(360);
        shopProductSpe8.setCreateTime(new Date());
        shopProductSpe8.setCreateBy(1L);

        ShopProductSpec shopProductSpe9= new ShopProductSpec();
        shopProductSpe9.setPrice(new BigDecimal(0));
        shopProductSpe9.setProdSpecId(productSpec9.getId());
        shopProductSpe9.setShopProdId(shopProduct.getProdId());
        shopProductSpe9.setSkuId(productSpec9.getSkuId());
        shopProductSpe9.setSpecName(productSpec9.getSpecName());
        shopProductSpe9.setStock(220);
        shopProductSpe9.setCreateTime(new Date());
        shopProductSpe9.setCreateBy(1L);

        ShopProductSpec shopProductSpe10= new ShopProductSpec();
        shopProductSpe10.setPrice(new BigDecimal(0));
        shopProductSpe10.setProdSpecId(productSpec10.getId());
        shopProductSpe10.setShopProdId(shopProduct.getProdId());
        shopProductSpe10.setSkuId(productSpec10.getSkuId());
        shopProductSpe10.setSpecName(productSpec10.getSpecName());
        shopProductSpe10.setStock(150);
        shopProductSpe10.setCreateTime(new Date());
        shopProductSpe10.setCreateBy(1L);

        List<ShopProductSpec> shopProductSpecs = new ArrayList<>();
        shopProductSpecs.add(shopProductSpec);
        shopProductSpecs.add(shopProductSpec2);
        shopProductSpecs.add(shopProductSpe3);
        shopProductSpecs.add(shopProductSpe44);
        shopProductSpecs.add(shopProductSpe4);
        shopProductSpecs.add(shopProductSpe5);
//        shopProductSpecs.add(shopProductSpe6);
        shopProductSpecs.add(shopProductSpe7);
        shopProductSpecs.add(shopProductSpe8);
        shopProductSpecs.add(shopProductSpe9);
        shopProductSpecs.add(shopProductSpe10);
        shopProductSpecService.saveBatch(shopProductSpecs);
    }
}
