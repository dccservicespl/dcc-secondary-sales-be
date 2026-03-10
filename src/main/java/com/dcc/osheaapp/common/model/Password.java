package com.dcc.osheaapp.common.model;

import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Password {

  private static final Logger LOGGER = LogManager.getLogger(Password.class);
  private static final int iterations = 1; // 20*1000
  private static final int saltLen = 32;
  private static final int desiredKeyLen = 256;

  /**
   * Computes a salted PBKDF2 hash of given plaintext password suitable for storing in a database.
   * Empty passwords are not supported.
   */
  public static String getSaltedHash(String password) throws Exception {
    LOGGER.info("getSaltedHash:: before generating salted hash seed");
    // byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
    // SecureRandom random = new SecureRandom();
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[saltLen];
    // random.nextBytes(salt);
    random.setSeed(salt);

    LOGGER.info("getSaltedHash:: after generating salted hash seed" + salt);
    LOGGER.info(
        "getSaltedHash:: after generating salted hash seed in Base64 string"
            + Base64.encodeBase64String(salt));
    // store the salt with the password
    return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
  }

  /**
   * Checks whether given plaintext password corresponds to a stored salted hash of the password.
   */
  public static boolean check(String password, String stored) throws Exception {
    String[] saltAndHash = stored.split("\\$");

    LOGGER.info("check:: salt And Hash lenght::" + saltAndHash.length);

    if (saltAndHash.length != 2) {
      throw new IllegalStateException("The stored password must have the form 'salt$hash'");
    }

    LOGGER.info("check:: hash seed before the comparison" + Base64.decodeBase64(saltAndHash[0]));
    String hashOfInput = hash(password, Base64.decodeBase64(saltAndHash[0]));
    return hashOfInput.equals(saltAndHash[1]);
  }

  // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
  // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
  private static String hash(String password, byte[] salt) throws Exception {
    if (password == null || password.length() == 0)
      throw new IllegalArgumentException("Empty passwords are not supported.");

    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    LOGGER.info("hash:: before generating key");
    SecretKey key =
        f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, desiredKeyLen));
    LOGGER.info("hash:: after generating key");

    return Base64.encodeBase64String(key.getEncoded());
  }
}
