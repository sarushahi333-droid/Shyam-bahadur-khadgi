package com.example

import com.example.data.seed.AuthorizedBibleData
import com.example.data.seed.BibleBooksData
import com.example.data.seed.DevotionalsData
import com.example.data.seed.StudyLessonsData
import com.example.service.AiBibleTeacherService
import com.example.service.SermonBuilderService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun bible_has66Books() {
        val books = BibleBooksData.allBooks
        assertEquals(66, books.size)
        assertEquals(39, books.count { it.testament == com.example.data.model.Testament.OLD })
        assertEquals(27, books.count { it.testament == com.example.data.model.Testament.NEW })
    }

    @Test
    fun studyLessons_has10CategorizedLessonsWith7Components() {
        val lessons = StudyLessonsData.lessons
        assertEquals(10, lessons.size)

        lessons.forEach { lesson ->
            assertTrue(lesson.topicIntro.isNotBlank())
            assertTrue(lesson.mainVerses.isNotEmpty())
            assertTrue(lesson.detailedExplanation.isNotBlank())
            assertTrue(lesson.realLifeExamples.isNotBlank())
            assertTrue(lesson.practicalApplications.isNotBlank())
            assertTrue(lesson.reflectionQuestions.isNotEmpty())
            assertTrue(lesson.closingPrayer.isNotBlank())
        }
    }

    @Test
    fun sermonBuilder_generatesCompleteSermonStructure() {
        val sermon = SermonBuilderService.generateSermon(
            topic = "विश्वासको विजय (Victory of Faith)",
            audience = "सम्पूर्ण मण्डली (Sunday Congregation)",
            durationMinutes = 30
        )
        assertNotNull(sermon)
        assertTrue(sermon.title.isNotBlank())
        assertTrue(sermon.mainPassage.isNotBlank())
        assertTrue(sermon.mainPoints.size >= 3)
        assertTrue(sermon.conclusion.isNotBlank())
        assertTrue(sermon.closingPrayer.isNotBlank())
    }

    @Test
    fun aiBibleTeacher_answersBasedOnScripture() {
        val response = AiBibleTeacherService.askQuestion("येशू ख्रीष्ट को हुनुहुन्छ?")
        assertTrue(response.exactBibleReferences.isNotEmpty())
        assertTrue(response.directAnswer.isNotBlank())
        assertTrue(response.historicalAndSpiritualContext.isNotBlank())
        assertTrue(response.scriptureComparisons.isNotBlank())
    }

    @Test
    fun devotionals_providesDailySpiritualFood() {
        val devotional = DevotionalsData.getTodayDevotional()
        assertNotNull(devotional)
        assertTrue(devotional.verseText.isNotBlank())
        assertTrue(devotional.prayer.isNotBlank())
    }
}
