package study.querydsl.domain.store;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = 37212990L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStore store = new QStore("store");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<study.querydsl.domain.menu.Menu, study.querydsl.domain.menu.QMenu> menus = this.<study.querydsl.domain.menu.Menu, study.querydsl.domain.menu.QMenu>createList("menus", study.querydsl.domain.menu.Menu.class, study.querydsl.domain.menu.QMenu.class, PathInits.DIRECT2);

    public final ListPath<study.querydsl.domain.order.Order, study.querydsl.domain.order.QOrder> orders = this.<study.querydsl.domain.order.Order, study.querydsl.domain.order.QOrder>createList("orders", study.querydsl.domain.order.Order.class, study.querydsl.domain.order.QOrder.class, PathInits.DIRECT2);

    public final QStoreCategory storeCategory;

    public final StringPath storeName = createString("storeName");

    public QStore(String variable) {
        this(Store.class, forVariable(variable), INITS);
    }

    public QStore(Path<? extends Store> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStore(PathMetadata metadata, PathInits inits) {
        this(Store.class, metadata, inits);
    }

    public QStore(Class<? extends Store> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.storeCategory = inits.isInitialized("storeCategory") ? new QStoreCategory(forProperty("storeCategory")) : null;
    }

}

