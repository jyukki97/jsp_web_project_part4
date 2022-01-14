package org.zerock.mreview.repository.search;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.zerock.mreview.entity.Movie;

public interface SearchMovieRepository {
    Movie search1();

    PageImpl<Movie> searchPage(String type, String keyword, Pageable pageable);
}
