package com.chotu.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chotu.notes.data.database.DatabaseProvider
import com.chotu.notes.repository.NoteRepository
import com.chotu.notes.ui.theme.NotesTheme
import com.chotu.notes.viewmodel.NotesViewModel
import com.chotu.notes.viewmodel.NotesViewModelFactory
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                NoteScreen()
            }
        }
    }
}


// Note card ke liye random pastel colors
val noteColors = listOf(
    Color(0xFFFFD6D6), // soft red
    Color(0xFFFFE8C8), // soft orange
    Color(0xFFFFF9C4), // soft yellow
    Color(0xFFD6F5D6), // soft green
    Color(0xFFD6EAFF), // soft blue
    Color(0xFFEDD6FF), // soft purple
    Color(0xFFFFD6F0), // soft pink
)

@Composable
fun NoteScreen(modifier: Modifier = Modifier) {
    var noteText by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val database = remember {
        DatabaseProvider.getDatabase(context)
    }

    val repository = remember {
        NoteRepository(database.noteDao())
    }

    val factory = remember {
        NotesViewModelFactory(repository)
    }

    val viewModel: NotesViewModel = viewModel(
        factory = factory
    )

    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6C63FF), Color(0xFF48CAE4)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "My Notes", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.White
            )
            Text(
                "${viewModel.notes.size} notes",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = {
                            noteText = it
                        },
                        placeholder = {
                            Text("Write Somethings...")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (noteText.isNotEmpty()) {
                                viewModel.insertNote(noteText)
                                noteText = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C63FF)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            "Save Note",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.notes.size) { index ->
                    val cardColor = noteColors[index % noteColors.size]

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardColor
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = Color.White.copy(alpha = 0.6f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${index + 1}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                viewModel.notes[index].title,
                                modifier = Modifier.weight(1f),
                                maxLines = 2,
                                fontSize = 15.sp,
                                overflow = TextOverflow.Ellipsis,
                                color = Color(0xFF222222)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            // ✨ Delete icon button (clean look)
                            IconButton(
                                onClick = {
                                    viewModel.deleteNote(
                                        viewModel.notes[index]
                                    )
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = Color.White.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFE53935),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteScreen1(modifier: Modifier = Modifier) {

    var noteText by remember {
        mutableStateOf("")
    }

    val notes = remember {
        mutableStateListOf<Note>()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top

    ) {
        Text(
            "Notes Apps", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = noteText, onValueChange = {
            noteText = it
        }, placeholder = {
            Text("Enter notes")
        })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (noteText.isNotEmpty()) {
                notes.add(
                    Note(
                        title = noteText
                    )
                )
                noteText = ""
            }
        }) {
            Text("Save Note")
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        LazyColumn {

            items(notes.size) { index ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = notes[index].title,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Button(
                            onClick = {
                                notes.removeAt(index)
                            }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotesTheme {
        NoteScreen()
    }
}