package com.android.cts.verifier.audio;

/**
 * Sound generator.
 */
public class SoundGenerator {

  private static SoundGenerator instance;

  private final byte[] generatedSound;
  private final double[] sample;

  private SoundGenerator() {
    // Initialize sample.
    int pipNum = Common.PIP_NUM;
    int prefixTotalLength = Common.PREFIX.length
        + Util.toLength(Common.PAUSE_BEFORE_PREFIX_DURATION_S, Common.getSampleRate())
        + Util.toLength(Common.PAUSE_AFTER_PREFIX_DURATION_S, Common.getSampleRate());
    int repetitionLength = pipNum * Util.toLength(
        Common.PIP_DURATION_S + Common.PAUSE_DURATION_S, Common.getSampleRate());
    int sampleLength = prefixTotalLength + Common.REPETITIONS * repetitionLength;
    sample = new double[sampleLength];

    // Fill sample with prefix.
    System.arraycopy(Common.PREFIX, 0, sample,
        Util.toLength(Common.PAUSE_BEFORE_PREFIX_DURATION_S, Common.getSampleRate()),
        Common.PREFIX.length);

    // Fill the sample.
    for (int i = 0; i < pipNum * Common.REPETITIONS; i++) {
      double[] pip = getPip(Common.generateWindow(), Common.FREQUENCIES[i]);
      System.arraycopy(pip, 0, sample,
          prefixTotalLength + i * Util.toLength(
              Common.PIP_DURATION_S + Common.PAUSE_DURATION_S, Common.getSampleRate()),
          pip.length);
    }

    // Convert sample to byte.
    generatedSound = new byte[2 * sample.length];
    int i = 0;
    for (double dVal : sample) {
      short val = (short) ((dVal * 32767));
      generatedSound[i++] = (byte) (val & 0x00ff);
      generatedSound[i++] = (byte) ((val & 0xff00) >>> 8);
    }
  }

  public static SoundGenerator getInstance() {
    if (instance == null) {
      instance = new SoundGenerator();
    }
    return instance;
  }

  /**
   * Gets a pip sample.
   */
  private static double[] getPip(double[] window, double frequency) {
    int pipArrayLength = window.length;
    double[] pipArray = new double[pipArrayLength];
    double radPerSample = 2 * Math.PI / (Common.getSampleRate() / frequency);
    for (int i = 0; i < pipArrayLength; i++) {
      pipArray[i] = window[i] * Math.sin(i * radPerSample);
    }
    return pipArray;
  }

  /**
   * Get generated sound in byte[].
   */
  public byte[] getByte() {
    return generatedSound;
  }

  /**
   * Get sample in double[].
   */
  public double[] getSample() {
    return sample;
  }
}
