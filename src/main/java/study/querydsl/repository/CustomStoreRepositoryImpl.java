package study.querydsl.repository;

import static com.querydsl.jpa.JPAExpressions.select;
import static study.querydsl.domain.order.QOrder.order;
import static study.querydsl.domain.order.QOrderMenu.orderMenu;
import static study.querydsl.domain.reivew.QReview.review;
import static study.querydsl.domain.store.QStore.store;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import study.querydsl.domain.order.QOrderMenu;
import study.querydsl.domain.reivew.QReview;
import study.querydsl.domain.store.StoreCategory;
import study.querydsl.repository.dto.QueryDslPopularStoreDto;

@RequiredArgsConstructor
public class CustomStoreRepositoryImpl implements CustomStoreRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<QueryDslPopularStoreDto> findPopularStoreThan(final long id) {
        return jpaQueryFactory.select(
                Projections.fields(
                        QueryDslPopularStoreDto.class,
                        store.as("store"),
                        orderMenu.quantity.sum().coalesce(0).as("totalMenuCount"),
                        review.rate.sum().coalesce(0).as("totalReviewScore"))
            )
            .from(store)
            .leftJoin(store.orders, order) // store를 기준으로 모든 order를 조회한다. (order가 없어도 조회가 되어야 함)
            .leftJoin(order.review, review)
            .leftJoin(order.orderMenus, orderMenu) // order 에 대해서 모든 ordermenu를 가져와야 한다.
            .where(store.id.ne(id), store.storeCategory.eq(getCategoryOfStore(id)))
            .groupBy(store.id)
            .having(filterMoreMenuCountThanStore(id).and(filterMoreOrEqualTotalRateThanStore(id)))
            .orderBy(review.rate.sum().desc())
            .fetch();

    }

    private JPQLQuery<StoreCategory> getCategoryOfStore(final long id) {
        return select(store.storeCategory)
            .from(store)
            .where(store.id.eq(id));
    }

    private BooleanExpression filterMoreMenuCountThanStore(final long id) {
        final QOrderMenu orderMenu2 = new QOrderMenu("orderMenu2");

        return orderMenu.quantity.sum().goe(
            select(orderMenu2.quantity.sum())
                .from(orderMenu2)
                .where(orderMenu2.order.store.id.eq(id))
        );
    }

    private BooleanExpression filterMoreOrEqualTotalRateThanStore(final long id) {
        final JPQLQuery<Integer> totalRateOfCurrentStore = getTotalRateOfStore(id);
        final NumberExpression<Integer> totalRateOfOthers = review.rate.sum();

        return totalRateOfOthers.gt(totalRateOfCurrentStore)
            .or(totalRateOfOthers.eq(totalRateOfCurrentStore).and(store.id.gt(id)));
    }

    private JPQLQuery<Integer> getTotalRateOfStore(final long id) {
        final QReview review2 = new QReview("review2");

        return select(review.rate.sum())
            .from(review2)
            .where(review2.order.store.id.eq(id));
    }
}
