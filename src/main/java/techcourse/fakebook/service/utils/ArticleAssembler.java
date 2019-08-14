package techcourse.fakebook.service.utils;

import techcourse.fakebook.controller.utils.SessionUser;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;

public class ArticleAssembler {
    private ArticleAssembler() {}

    public static Article toEntity(ArticleRequest articleRequest, User user) {
        return new Article(articleRequest.getContent(), user);
    }

    public static ArticleResponse toResponse(Article article) {
        SessionUser sessionUser = UserAssembler.toSessionUser(article.getUser());
        return new ArticleResponse(article.getId(), article.getContent(), sessionUser);
    }
}
