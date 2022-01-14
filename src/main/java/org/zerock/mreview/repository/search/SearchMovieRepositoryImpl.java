package org.zerock.mreview.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.QMovie;

import java.util.List;

@Log4j2
public class SearchMovieRepositoryImpl extends QuerydslRepositorySupport implements SearchMovieRepository {
    public SearchMovieRepositoryImpl() {
        super(Movie.class);
    }

    @Override
    public Movie search1() {

        log.info("search1........................");

        QMovie movie = QMovie.movie;

        JPQLQuery<Movie> jpqlQuery = from(movie);
        
        log.info("---------------------------");
        log.info(jpqlQuery);
        log.info("---------------------------");

        List<Movie> result = jpqlQuery.fetch();

        log.info(result);

        return null;
    }

    @Override
    public PageImpl<Movie> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage.............................");

        QMovie movie = QMovie.movie;

        JPQLQuery<Movie> jpqlQuery = from(movie);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = movie.mno.gt(0L);

        booleanBuilder.and(expression);

        if(keyword != null){
            booleanBuilder.and(movie.title.contains(keyword));
        }

        jpqlQuery.where(booleanBuilder);

        //order by
        Sort sort = pageable.getSort();

        //tuple.orderBy(board.bno.desc());

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Movie.class, "movie");
            jpqlQuery.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });

        //page 처리
        jpqlQuery.offset(pageable.getOffset());
        jpqlQuery.limit(pageable.getPageSize());

        List<Movie> result = jpqlQuery.fetch();

        log.info(result);

        long count = jpqlQuery.fetchCount();

        log.info("COUNT: " +count);

        return  new PageImpl<Movie>(
                result,
                pageable,
                count);
    }
}
