package io.recruitment.assessment.api.service;

import com.google.common.eventbus.EventBus;
import io.recruitment.assessment.api.dto.Product;
import io.recruitment.assessment.api.dto.User;
import io.recruitment.assessment.api.entity.ProductEntity;
import io.recruitment.assessment.api.event.Event;
import io.recruitment.assessment.api.exception.InternalErrorException;
import io.recruitment.assessment.api.exception.ResourceNotFoundException;
import io.recruitment.assessment.api.repository.ProductRepository;
import io.recruitment.assessment.api.utils.PageQuery;
import io.recruitment.assessment.api.utils.PaginationResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "caffeineCacheManager")
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    private EventBus eventBus;

    private static ModelMapper modelMapper = new ModelMapper();


    /**
     * @Description Get Product by product id
     * @param productId
     * @return
     * @Todo: Cache
     */
    @Cacheable(key = "#productId")
    public Product getProductById(Integer productId) {
        ProductEntity productEntity = productRepository.findById(productId);
        if (productEntity == null) {
            throw new ResourceNotFoundException("Cannot find product: " + productId);
        }
        Product product = modelMapper.map(productEntity, Product.class);
        return product;
    }

    /**
     * @Description Get Products
     * @param productIds
     * @return
     */
    public List<Product> getProducts(List<Integer> productIds) {
        List<ProductEntity> productEntities = productRepository.findProductList(productIds);
        if (CollectionUtils.isEmpty(productEntities)) {
            throw new ResourceNotFoundException("Cannot find products: " + productIds.toString());
        }
        List<Product> products = new ArrayList<>();
        productEntities.forEach(productEntity -> products.add(modelMapper.map(productEntity, Product.class)));
        return products;
    }


    /**
     * @Description Search Products
     * @param pageQuery
     * @return
     */
    public PaginationResult searchProducts(PageQuery pageQuery) {
        List<ProductEntity> productEntityList = productRepository.findProductsListBySearch(pageQuery);
        List<Product> productList = new ArrayList<>();
        productEntityList.forEach(productEntity -> productList.add(modelMapper.map(productEntity, Product.class)));
        int total = productRepository.findTotalProductsBySearch(pageQuery.getKeyword());

        return new PaginationResult(productList, total, pageQuery.getLimit(), pageQuery.getPage());
    }


    /**
     * @Description Save one record of Product
     * @param product
     * @param user
     * @return
     */
    @CachePut(key = "#product.productId")
    public Product saveProduct(Product product, User user) {
        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
        productRepository.save(productEntity, user.getUserId());
        product.setProductId(productEntity.getProductId());

        return product;
    }


    /**
     * @Description Update one record of Product
     * @param product
     * @param user
     * @return
     */
    @Transactional
    @CachePut(key = "#product.productId")
    public Product updateProduct(Product product, User user) {
        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
        ProductEntity productEntityLast = productRepository.findById(product.getProductId());
        if (productEntityLast.getVersion() != product.getVersion()-1){
            throw new InternalErrorException("Version conflict.");
        }
        productRepository.update(productEntity, user.getUserId());

        // Record the product's price change as event
        Map<String, String> msg = new HashMap<>();
        msg.put("previous_price", productEntityLast.getPrice().toString());
        Event event = new Event(product.getProductId(), msg, user.getUserId());
        eventBus.post(event);

        return modelMapper.map(productEntity, Product.class);
    }


    /**
     * @Description Delete Products
     * @param productIds
     */
    @Transactional
    public void deleteProducts(Integer[] productIds) {
        List<Product> isDeletedProducts = getProducts(Arrays.asList(productIds))
                .stream().filter(product -> product.getDeleteFlg() != 0).collect(Collectors.toList());
        if (isDeletedProducts.size() != 0) {
            throw new IllegalArgumentException("Cannot delete non-existed product.");
        }
        productRepository.deleteProducts(productIds);
    }


    /**
     * @Description Update Product stock number
     * @param stockNumber
     * @param productId
     * @return
     */
    public Integer updateProductStockNumber(Integer stockNumber, Integer productId) {
        return productRepository.updateStockNumber(stockNumber, productId);
    }

}
