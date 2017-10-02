package com.github.scadete.regula.stt;

import java.io.IOException;

public interface SpeechToTextService {
    public String convert(String voiceUrl) throws Exception;
}
