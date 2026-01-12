package org.example.whiskr.util

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import whiskr.features.posts.generated.resources.Res
import whiskr.features.posts.generated.resources.time_just_now
import whiskr.features.posts.generated.resources.time_short_day
import whiskr.features.posts.generated.resources.time_short_hour
import whiskr.features.posts.generated.resources.time_short_minute
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun rememberRelativeTime(date: LocalDateTime): String {
    val instant = date.toInstant(TimeZone.UTC)

    val now = Clock.System.now()

    val duration = now - instant
    val seconds = duration.inWholeSeconds
    val minutes = duration.inWholeMinutes
    val hours = duration.inWholeHours
    val days = duration.inWholeDays

    return when {
        seconds < 60 -> stringResource(Res.string.time_just_now)
        minutes < 60 -> stringResource(Res.string.time_short_minute, minutes)
        hours < 24 -> stringResource(Res.string.time_short_hour, hours)
        days < 7 -> stringResource(Res.string.time_short_day, days)
        else -> {
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            formatDate(localDateTime)
        }
    }
}

private fun formatDate(date: LocalDateTime): String {
    val day = date.day.toString().padStart(2, '0')
    val month = date.month.number.toString().padStart(2, '0')
    val year = date.year
    return "$day.$month.$year"
}