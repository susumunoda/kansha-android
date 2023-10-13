package com.susumunoda.kansha.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.susumunoda.compose.material3.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.R
import com.susumunoda.kansha.repository.note.Note
import com.susumunoda.kansha.repository.user.User
import com.susumunoda.kansha.ui.component.DefaultUserPhoto
import com.susumunoda.kansha.ui.component.UserPhoto
import com.susumunoda.kansha.ui.mock.MockProvider
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    ScaffoldWithStatusBarInsets(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.profile_title)) }
            )
        }
    ) {
        Column {
            UserInfoSection(uiState.user, uiState.error)
            NotesInfoSection(uiState.notes)
        }
    }
}

@Composable
private fun UserInfoSection(user: User?, error: Exception?) {
    Box(contentAlignment = Alignment.Center) {
        if (user != null) {
            if (user.backgroundPhotoUrl.isNotBlank()) {
                AsyncImage(
                    model = user.backgroundPhotoUrl,
                    contentDescription = stringResource(R.string.profile_background_photo_description),
                    modifier = Modifier.height(dimensionResource(R.dimen.profile_background_photo_height)),
                    contentScale = ContentScale.Crop,
                    // Only for displaying in an @Preview
                    placeholder = if (LocalInspectionMode.current) {
                        painterResource(R.drawable.preview_background_photo)
                    } else null
                )
            } else {
                Spacer(Modifier.height(dimensionResource(R.dimen.profile_background_photo_height)))
            }
            if (user.profilePhotoUrl.isBlank()) {
                DefaultUserPhoto(
                    size = dimensionResource(R.dimen.profile_photo_size_large)
                )
            } else {
                UserPhoto(
                    url = user.profilePhotoUrl,
                    size = dimensionResource(R.dimen.profile_photo_size_large),
                    // Only for displaying in an @Preview
                    placeholder = if (LocalInspectionMode.current) {
                        painterResource(R.drawable.preview_profile_photo)
                    } else null
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.2f))
                    .height(dimensionResource(R.dimen.profile_name_background_height))
                    .fillMaxWidth()
            ) {
                Text(
                    user.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
        } else if (error != null) {
            Box(Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
                Text(
                    stringResource(R.string.profile_fetch_error, error.message ?: ""),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun NotesInfoSection(notes: List<Note>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = stringResource(R.string.profile_notes_added_header),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
        )

        val lastWeekCalendar = Calendar.getInstance()
        lastWeekCalendar.add(Calendar.DATE, -7)
        val lastWeekNotes = notes.filter { it.createdAt > lastWeekCalendar.time }
        val lastWeekCategories = lastWeekNotes.map { it.categoryId }.distinct()
        Text("${pluralizeNotes(lastWeekNotes.size)} in ${pluralizeCategories(lastWeekCategories.size)} in the last week")

        val lastMonthCalendar = Calendar.getInstance()
        lastMonthCalendar.add(Calendar.MONTH, -1)
        val lastMonthNotes = notes.filter { it.createdAt > lastMonthCalendar.time }
        val lastMonthCategories = lastMonthNotes.map { it.categoryId }.distinct()
        Text("${pluralizeNotes(lastMonthNotes.size)} in ${pluralizeCategories(lastMonthCategories.size)} in the last month")

        val lastYearCalendar = Calendar.getInstance()
        lastYearCalendar.add(Calendar.YEAR, -1)
        val lastYearNotes = notes.filter { it.createdAt > lastYearCalendar.time }
        val lastYearCategories = lastYearNotes.map { it.categoryId }.distinct()
        Text("${pluralizeNotes(lastYearNotes.size)} in ${pluralizeCategories(lastYearCategories.size)} in the last year")
    }
}

// FUTURE: Use quantity strings (https://developer.android.com/guide/topics/resources/string-resource#Plurals)
private fun pluralizeNotes(numNotes: Int) = if (numNotes == 1) "1 note" else "$numNotes notes"
private fun pluralizeCategories(numCategories: Int) =
    if (numCategories == 1) "1 category" else "$numCategories categories"

@Preview
@Composable
fun ProfileScreenPreview() {
    val mockProvider = MockProvider().apply { userRepositoryDatabase[sessionUserId] = user }
    val authController = mockProvider.authController
    val userRepository = mockProvider.userRepository
    val noteRepository = mockProvider.noteRepository
    val viewModel = ProfileScreenViewModel(authController, userRepository, noteRepository)
    ProfileScreen(viewModel)
}

@Preview
@Composable
fun ProfileScreenErrorPreview() {
    val mockProvider = MockProvider().apply { userRepositoryErrorOnGetUser = true }
    val authController = mockProvider.authController
    val userRepository = mockProvider.userRepository
    val noteRepository = mockProvider.noteRepository
    val viewModel = ProfileScreenViewModel(authController, userRepository, noteRepository)
    ProfileScreen(viewModel)
}
