package com.vocab2000fa.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vocab2000fa.data.AppDatabase
import com.vocab2000fa.data.WordRepository
import com.vocab2000fa.ui.screens.*

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Levels : Route("levels")
    data object Lessons : Route("lessons/{level}") {
        fun create(level: Int) = "lessons/$level"
    }
    data object LessonDetail : Route("lesson/{level}/{lesson}") {
        fun create(level: Int, lesson: Int) = "lesson/$level/$lesson"
    }
    data object Word : Route("word/{id}") {
        fun create(id: Int) = "word/$id"
    }
    data object Search : Route("search")
    data object Favorites : Route("favorites")
    data object Quiz : Route("quiz")
}

@Composable
fun AppRoot(db: AppDatabase) {
    val nav = rememberNavController()
    val repo = remember(db) { WordRepository(db.wordDao()) }

    NavHost(navController = nav, startDestination = Route.Home.path) {

        composable(Route.Home.path) {
            HomeScreen(
                repo = repo,
                onOpenLevels = { nav.navigate(Route.Levels.path) },
                onOpenSearch = { nav.navigate(Route.Search.path) },
                onOpenFavorites = { nav.navigate(Route.Favorites.path) },
                onOpenQuiz = { nav.navigate(Route.Quiz.path) }
            )
        }

        composable(Route.Levels.path) {
            LevelsScreen(
                repo = repo,
                onBack = { nav.popBackStack() },
                onOpenLevel = { level -> nav.navigate(Route.Lessons.create(level)) }
            )
        }

        composable(
            Route.Lessons.path,
            arguments = listOf(navArgument("level") { type = NavType.IntType })
        ) { backStackEntry ->
            val level = backStackEntry.arguments?.getInt("level") ?: 1
            LessonsScreen(
                level = level,
                repo = repo,
                onBack = { nav.popBackStack() },
                onOpenLesson = { l -> nav.navigate(Route.LessonDetail.create(level, l)) }
            )
        }

        composable(
            Route.LessonDetail.path,
            arguments = listOf(
                navArgument("level") { type = NavType.IntType },
                navArgument("lesson") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val level = backStackEntry.arguments?.getInt("level") ?: 1
            val lesson = backStackEntry.arguments?.getInt("lesson") ?: 1
            LessonDetailScreen(
                level = level,
                lesson = lesson,
                repo = repo,
                onBack = { nav.popBackStack() },
                onOpenWord = { id -> nav.navigate(Route.Word.create(id)) }
            )
        }

        composable(
            Route.Word.path,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            WordDetailScreen(
                id = id,
                repo = repo,
                onBack = { nav.popBackStack() }
            )
        }

        composable(Route.Search.path) {
            SearchScreen(
                repo = repo,
                onBack = { nav.popBackStack() },
                onOpenWord = { id -> nav.navigate(Route.Word.create(id)) }
            )
        }

        composable(Route.Favorites.path) {
            FavoritesScreen(
                repo = repo,
                onBack = { nav.popBackStack() },
                onOpenWord = { id -> nav.navigate(Route.Word.create(id)) }
            )
        }

        composable(Route.Quiz.path) {
            QuizScreen(
                repo = repo,
                onBack = { nav.popBackStack() },
                onOpenWord = { id -> nav.navigate(Route.Word.create(id)) }
            )
        }
    }
}