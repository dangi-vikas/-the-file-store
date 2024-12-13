package com.project.spring.the_file_store;

import com.project.spring.the_file_store.controllers.FileStore;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class TheFileStoreApplicationTests {
	public static final String FILESTORE = "/filestore";
	@Autowired
		private MockMvc mockMvc;

		@MockitoBean
		private FileStore fileStoreController;

		@Test
		void addFileShouldAddFileWhenFileNameAndContentIsPassed() throws Exception {
			String fileName = "file2.txt";
			String fileContent = "Hello, World!";
			byte[] content = fileContent.getBytes();
			String hash = "545fgfhggfdfskslss";
			when(fileStoreController.addFile(fileName, hash,  content)).thenReturn(
					ResponseEntity.status(HttpStatus.CREATED).body("File added successfully."));

			mockMvc.perform(post(FILESTORE + "/add")
							.param("fileName", fileName)
							.param("hash", hash)
							.content(content)
							.contentType(MediaType.ALL))
					.andExpect(status().isCreated())
					.andExpect(content().string("File added successfully."));

			verify(fileStoreController, times(1)).addFile(fileName, hash, content);
		}

		@Test
		void listFilesShouldReturnAllTheFilesInDirectory() throws Exception {
			String[] mockFileNames = new String[]{"file1.txt", "file2.txt"};
			when(fileStoreController.listFiles()).thenReturn(ResponseEntity.ok(String.join("\n", mockFileNames)));

			mockMvc.perform(get(FILESTORE + "/list"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$").isString())
					.andExpect(jsonPath("$").value(String.join("\n", mockFileNames)));

			verify(fileStoreController, times(1)).listFiles();
		}

		@Test
		void removeFileShouldRemoveFileFromSeverWhenFileNameIsGiven() throws Exception {
			String fileName = "file1.txt";
			when(fileStoreController.removeFile(fileName)).thenReturn(ResponseEntity.ok("File removed successfully."));

			mockMvc.perform(delete(FILESTORE + "/remove")
							.param("fileName", fileName))
					.andExpect(status().isOk())
					.andExpect(content().string("File removed successfully."));

			verify(fileStoreController, times(1)).removeFile(fileName);
		}

		@Test
		void updateFileShouldUpdateTheContentsOfFileWhenFileNameIsGiven() throws Exception {
			String fileName = "file1.txt";
			String updatedContent = "Updated Content";
			byte[] content = updatedContent.getBytes();
			String hash = "545fgfhggfdfskslss";
			when(fileStoreController.updateFile(fileName, hash, content)).thenReturn(
					ResponseEntity.ok("File updated successfully."));

			mockMvc.perform(put(FILESTORE + "/update")
							.param("fileName", fileName)
							.param("hash", hash)
							.content(updatedContent)
							.contentType(MediaType.ALL))
					.andExpect(status().isOk())
					.andExpect(content().string("File updated successfully."));

			verify(fileStoreController, times(1)).updateFile(fileName, hash, content);
		}

		@Test
		void wordCountShouldReturnCountOfAllTheWordsInFiles() throws Exception {
			when(fileStoreController.wordCount()).thenReturn(ResponseEntity.ok("Total words: " + 42));

			mockMvc.perform(get(FILESTORE + "/wc"))
					.andExpect(status().isOk())
					.andExpect(content().string("Total words: 42"));

			verify(fileStoreController, times(1)).wordCount();
		}

		@Test
		void testFrequentWords() throws Exception {
			String mockResult = "Hello : 1" + "\n" + "World: 1";
			when(fileStoreController.frequentWords(2, "dsc")).thenReturn(ResponseEntity.ok(mockResult));

			mockMvc.perform(get(FILESTORE + "/freq-words")
							.param("limit", "2")
							.param("order", "dsc"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$").isString())
					.andExpect(jsonPath("$").value(mockResult));

			verify(fileStoreController, times(1)).frequentWords(2, "dsc");
		}
	}

