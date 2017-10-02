package com.github.scadete.regula.stt.google;

import com.github.scadete.regula.stt.SpeechToTextService;
import com.google.api.gax.rpc.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.longrunning.Operation;
import com.google.protobuf.ByteString;
import net.sourceforge.javaflacencoder.FLAC_FileEncoder;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Component("googleSpeechToTextService")
public class GoogleSpeechToTextService implements SpeechToTextService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleSpeechToTextService.class);

    @Override
    public String convert(String audioUrl) throws Exception {

        String tempExecId = UUID.randomUUID().toString();
        File tempInputFile = File.createTempFile("temp_" + tempExecId, "_in.mp4");
        File tempOutputFile = File.createTempFile("temp_" + tempExecId, "_out.flac");
        logger.debug("Saving audio file: " + tempInputFile.getAbsolutePath());


        URL website = new URL(audioUrl);
        try (InputStream in = website.openStream()) {
            Files.copy(in, tempInputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();
        flacEncoder.encode(tempInputFile, tempOutputFile);

        SpeechClient speech = SpeechClient.create();

        // Configure remote file request for Linear16
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                .setLanguageCode("pt-BR")
                .setSampleRateHertz(8000)
                .build();
        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(Files.readAllBytes(tempOutputFile.toPath())))
                .build();

        OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata,
                        Operation> response =
                speech.longRunningRecognizeAsync(config, audio);
        while (!response.isDone()) {
            System.out.println("Waiting for response...");
            Thread.sleep(5000);
        }

        List<SpeechRecognitionResult> results = response.get().getResultsList();

        speech.close();

        return results.get(0).toString();
    }

    public static void main(String[] args) {
        GoogleSpeechToTextService stt = new GoogleSpeechToTextService();
        try {
            String text = stt.convert("https://cdn.fbsbx.com/v/t59.3654-21/21753671_10214150198169909_5776322024859238400_n.mp4/audioclip-1506984727000-2944.mp4?oh=d6a062d2d48758fa5708062ca705f277&oe=59D4E3FE");
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
