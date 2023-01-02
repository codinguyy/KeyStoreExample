package de.addmoremobile.lidlconnect.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import app.codinguyy.keystoreexample.util.SharedPreferences
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class KeyStoreUtils(val sharedPreferences: SharedPreferences) {
    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.ENCRYPT_MODE, createKey())
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry("secret", null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder("secret", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    fun encrypt(bytes: ByteArray): ByteArray {
        val encryptBytes = encryptCipher.doFinal(bytes)
        saveData(encryptBytes, encryptCipher.iv)
        return encryptBytes
    }

    fun decrypt(): String {
        val ivByteArray = Base64.decode(sharedPreferences.iv, Base64.DEFAULT)
        val encryptedPassword = Base64.decode(sharedPreferences.password, Base64.DEFAULT)

        val keystore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        val secretkey = keyStore.getKey("secret", null)
        val cipher = getDecryptCipherForIv(ivByteArray)
        cipher.init(Cipher.DECRYPT_MODE, secretkey, IvParameterSpec(ivByteArray))
        val bytePassword = cipher.doFinal(encryptedPassword)
        return bytePassword?.decodeToString() ?: ("")
    }

    private fun saveData(encryptedPassword: ByteArray, encryptionIv: ByteArray) {
        sharedPreferences.password = Base64.encodeToString(encryptedPassword, Base64.DEFAULT)
        sharedPreferences.iv = Base64.encodeToString(encryptionIv, Base64.DEFAULT)
    }
}
