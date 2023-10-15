package study.querydsl.domain.store;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStoreCategory is a Querydsl query type for StoreCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreCategory extends EntityPathBase<StoreCategory> {

    private static final long serialVersionUID = 841274972L;

    public static final QStoreCategory storeCategory = new QStoreCategory("storeCategory");

    public final StringPath category = createString("category");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QStoreCategory(String variable) {
        super(StoreCategory.class, forVariable(variable));
    }

    public QStoreCategory(Path<? extends StoreCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStoreCategory(PathMetadata metadata) {
        super(StoreCategory.class, metadata);
    }

}

