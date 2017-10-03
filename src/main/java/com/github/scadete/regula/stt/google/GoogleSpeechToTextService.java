package com.github.scadete.regula.stt.google;

import com.github.scadete.regula.stt.SpeechToTextService;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

    private static final int SAMPLE_BIT_RATE = 16000;

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

        FFmpeg ffmpeg = new FFmpeg("ffmpeg");
        FFprobe ffprobe = new FFprobe("ffprobe");

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(tempInputFile.getAbsolutePath())     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput(tempOutputFile.getAbsolutePath())
                .setAudioSampleRate(SAMPLE_BIT_RATE)
                .setAudioChannels(1)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Run a one-pass encode
        executor.createJob(builder).run();

        //ServiceAccountCredentials.newBuilder().setClientEmail().build().;
        File credentialsFile = new File("google-credentials.json");

        if(!credentialsFile.exists()) {
            String content = System.getenv("GOOGLE_CREDENTIALS");
            FileUtils.writeStringToFile(credentialsFile, content, "UTF-8");
        }


        SpeechClient speech = SpeechClient.create();
        // Configure remote file request for Linear16
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                .setLanguageCode("pt-BR")
                .setSampleRateHertz(SAMPLE_BIT_RATE)
                .build();
        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(Files.readAllBytes(tempOutputFile.toPath())))
                .build();

        RecognizeResponse response = speech.recognize(config, audio);
        List<SpeechRecognitionResult> results = response.getResultsList();

        speech.close();

        FileUtils.deleteQuietly(tempInputFile);
        FileUtils.deleteQuietly(tempOutputFile);

        return results.get(0).getAlternatives(0).getTranscript();
    }
}
