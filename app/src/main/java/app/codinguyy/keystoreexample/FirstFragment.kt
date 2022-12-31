package app.codinguyy.keystoreexample

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.codinguyy.keystoreexample.databinding.FragmentFirstBinding
import de.addmoremobile.lidlconnect.utils.KeyStoreUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var keyStoreUtils = KeyStoreUtils()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edittext.setText("This is a test")

        binding.buttonDecrypt.setOnClickListener {
            context?.let {
                val decryptedText = decrypt(it)
                binding.textviewEncryptes.text = decryptedText
            }
        }
        binding.buttonEncrypt.setOnClickListener {
             keyStoreUtils = KeyStoreUtils()
            context?.let {
                val inputText = binding.edittext.text.toString()
                val encryptedText = encrypt(inputText, it)
                binding.textviewEncryptes.text=encryptedText
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun decrypt(context: Context): String {
        val file = File(context.filesDir, "secret.txt")
        return keyStoreUtils.decrypt(FileInputStream(file)).decodeToString()
    }

    fun encrypt(data: String, context: Context): String {
        val byteArray = data.encodeToByteArray()

        // safe encrypted data not in file- save in sharedpreferernces

        val file = File(context.filesDir, "secret.txt")
        file.createNewFile()
        val fos = FileOutputStream(file)

        return keyStoreUtils.encrypt(byteArray, fos).decodeToString()
    }
}
