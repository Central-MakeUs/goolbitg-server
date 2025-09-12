package com.goolbitg.api.v1.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroup;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupEnrollment;
import com.goolbitg.api.v1.entity.challengeGroup.enumeration.EnrollmentStatus;
import com.goolbitg.api.v1.config.QueryDslConfig;

@Disabled
@DataJpaTest
@Import({ChallengeGroupCustomRepository.class, QueryDslConfig.class})
@ActiveProfiles("test")
class ChallengeGroupCustomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChallengeGroupCustomRepository repository;

    private ChallengeGroup challengeGroup1;
    private ChallengeGroup challengeGroup2;
    private ChallengeGroup challengeGroup3;

    @BeforeEach
    void setUp() {
        // Create and persist test data
        challengeGroup1 = ChallengeGroup.builder()
            .title("Java Programming Challenge")
            .hashtags("java,programming,coding")
            .ownerId("owner1")
            .maxSize(10)
            .reward(1000)
            .isHidden(false)
            .build();

        challengeGroup2 = ChallengeGroup.builder()
            .title("Python Data Science")
            .hashtags("python,datascience,ml")
            .ownerId("owner2")
            .maxSize(15)
            .reward(2000)
            .isHidden(false)
            .build();

        challengeGroup3 = ChallengeGroup.builder()
            .title("Web Development Bootcamp")
            .hashtags("web,javascript,frontend")
            .ownerId("owner1")
            .maxSize(20)
            .reward(1500)
            .isHidden(false)
            .build();

        ChallengeGroupEnrollment enrollment1 = ChallengeGroupEnrollment.builder()
            .userId("owner1")
            .groupId(challengeGroup2.getId())
            .status(EnrollmentStatus.ENROLL)
            .build();

        entityManager.persist(challengeGroup1);
        entityManager.persist(challengeGroup2);
        entityManager.persist(challengeGroup3);

        entityManager.persist(enrollment1);

        entityManager.flush();
    }

    @Test
    void search_withNullSearchAndNullOwnerId_shouldReturnAllResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ChallengeGroup> result = repository.search(null, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(3L);
        assertThat(result.getPageable()).isEqualTo(pageable);
        assertThat(result.getContent()).extracting("title")
            .containsExactlyInAnyOrder(
                "Java Programming Challenge",
                "Python Data Science",
                "Web Development Bootcamp"
            );
    }

    @Test
    void search_withEmptySearchAndBlankOwnerId_shouldReturnAllResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 5);

        // When
        Page<ChallengeGroup> result = repository.search("", "   ", null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(3L);
    }

    @Test
    void search_withValidSearchTermInTitle_shouldReturnMatchingResults() {
        // Given
        String searchTerm = "java";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ChallengeGroup> result = repository.search(searchTerm, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1L);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Java Programming Challenge");
    }

    @Test
    void search_withValidSearchTermInHashtags_shouldReturnMatchingResults() {
        // Given
        String searchTerm = "programming";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ChallengeGroup> result = repository.search(searchTerm, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1L);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Java Programming Challenge");
    }

    @Test
    void search_withValidOwnerId_shouldReturnOwnerResults() {
        // Given
        String ownerId = "owner1";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ChallengeGroup> result = repository.search(null, ownerId, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2L);
        assertThat(result.getContent()).extracting("ownerId")
            .containsOnly("owner1");
        assertThat(result.getContent()).extracting("title")
            .containsExactlyInAnyOrder(
                "Java Programming Challenge",
                "Web Development Bootcamp"
            );
    }

    @Test
    void search_withBothSearchTermAndOwnerId_shouldApplyBothFilters() {
        // Given
        String searchTerm = "programming";
        String ownerId = "owner1";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ChallengeGroup> result = repository.search(searchTerm, ownerId, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1L);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Java Programming Challenge");
        assertThat(result.getContent().get(0).getOwnerId()).isEqualTo("owner1");
    }

    @Test
    void search_withPagination_shouldApplyCorrectOffsetAndLimit() {
        // Given
        Pageable pageable = PageRequest.of(1, 2); // Page 1, size 2, so offset should be 2

        // When
        Page<ChallengeGroup> result = repository.search(null, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1); // Only 1 item left on page 1
        assertThat(result.getTotalElements()).isEqualTo(3L);
        assertThat(result.getNumber()).isEqualTo(1); // Current page number
        assertThat(result.getSize()).isEqualTo(2);   // Page size
        assertThat(result.getTotalPages()).isEqualTo(2); // 3 total / 2 size = 2 pages (rounded up)
    }

    @Test
    void search_withNonExistentSearchTerm_shouldReturnEmptyPage() {
        // Given
        String searchTerm = "nonexistent";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ChallengeGroup> result = repository.search(searchTerm, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0L);
        assertThat(result.hasContent()).isFalse();
    }

    @Test
    void search_withNonExistentOwnerId_shouldReturnEmptyPage() {
        // Given
        String ownerId = "nonexistent";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ChallengeGroup> result = repository.search(null, ownerId, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0L);
        assertThat(result.hasContent()).isFalse();
    }

    @Test
    void search_withFirstPage_shouldHaveCorrectPageProperties() {
        // Given
        Pageable pageable = PageRequest.of(0, 2);

        // When
        Page<ChallengeGroup> result = repository.search(null, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isFalse();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.hasPrevious()).isFalse();
        assertThat(result.getTotalPages()).isEqualTo(2); // 3 total / 2 size = 2 pages
    }

    @Test
    void search_withLastPage_shouldHaveCorrectPageProperties() {
        // Given
        Pageable pageable = PageRequest.of(1, 2); // Last page

        // When
        Page<ChallengeGroup> result = repository.search(null, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isFirst()).isFalse();
        assertThat(result.isLast()).isTrue();
        assertThat(result.hasNext()).isFalse();
        assertThat(result.hasPrevious()).isTrue();
    }

    @Test
    void search_searchTermIsCaseInsensitive_shouldMatchRegardlessOfCase() {
        // Given
        String searchTermLower = "java";
        String searchTermUpper = "JAVA";
        String searchTermMixed = "JaVa";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ChallengeGroup> resultLower = repository.search(searchTermLower, null, null, pageable);
        Page<ChallengeGroup> resultUpper = repository.search(searchTermUpper, null, null, pageable);
        Page<ChallengeGroup> resultMixed = repository.search(searchTermMixed, null, null, pageable);

        // Then
        assertThat(resultLower.getContent()).hasSize(1);
        assertThat(resultUpper.getContent()).hasSize(1);
        assertThat(resultMixed.getContent()).hasSize(1);

        // All should return the same result
        assertThat(resultLower.getContent().get(0).getTitle())
            .isEqualTo(resultUpper.getContent().get(0).getTitle())
            .isEqualTo(resultMixed.getContent().get(0).getTitle())
            .isEqualTo("Java Programming Challenge");
    }

    @Test
    void search_searchInHashtagsWithCommaDelimiter_shouldMatchCorrectly() {
        // Given
        String searchTerm = "datascience";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ChallengeGroup> result = repository.search(searchTerm, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Python Data Science");
        assertThat(result.getContent().get(0).getHashtags()).contains("datascience");
    }

    @Test
    void search_searchParticipating_shouldMatchCorrectly() {
        // Given
        String userId = "owner1";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ChallengeGroup> result = repository.search(null, null, userId, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(challengeGroup2);
    }
}
