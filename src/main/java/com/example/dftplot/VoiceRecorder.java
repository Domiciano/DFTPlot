package com.example.dftplot;

import javax.sound.sampled.*;

public class VoiceRecorder {

    public static double[] record(int fs) {
        // Recording duration in seconds
        int durationSeconds = 3;

        // Audio format
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                fs, 16, 1, 2, fs, false);

        // Configure the input line for recording
        TargetDataLine line = null;
        try {
            line = AudioSystem.getTargetDataLine(format);
            line.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        // Size of the audio buffer
        int bufferSize = (int) (format.getFrameSize() * format.getFrameRate() * durationSeconds);

        // Initialize the array to store audio samples
        double[] audioSamples = new double[bufferSize / 2]; // Divided by 2 to convert bytes to 16-bit samples

        // Start recording
        System.out.println("Recording...");
        line.start();

        // Read the recording data and store the samples
        byte[] buffer = new byte[bufferSize];
        int numBytesRead = line.read(buffer, 0, buffer.length);
        for (int i = 0, j = 0; i < numBytesRead; i += 2, j++) {
            // Convert bytes to 16-bit sample
            short sample = (short) ((buffer[i] & 0xFF) | (buffer[i + 1] << 8));
            // Store the magnitude of the sample
            audioSamples[j] = sample; // Taking the absolute value to ensure it's positive
        }

        // Stop recording
        line.stop();
        line.close();
        System.out.println("Recording finished.");

        // Print the number of read samples
        System.out.println("Audio samples read: " + audioSamples.length);
        return audioSamples;
    }

}
