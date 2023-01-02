package app.codinguyy.keystoreexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.codinguyy.keystoreexample.databinding.FragmentFirstBinding
import app.codinguyy.keystoreexample.util.SharedPreferences
import de.addmoremobile.lidlconnect.utils.KeyStoreUtils

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val keyStoreUtils by lazy {
        context?.let {
            sharedPref?.let { sharedPref ->
                KeyStoreUtils(it, sharedPref)
            }
        }
    }
    private val binding get() = _binding!!
    private val sharedPref by lazy {
        context?.let {
            SharedPreferences(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edittext.setText("This is a test")

        binding.buttonDecrypt.setOnClickListener {
            val decryptedText = decrypt()
            binding.textviewEncryptes.text = decryptedText
        }
        binding.buttonEncrypt.setOnClickListener {
            context?.let {
                val inputText = binding.edittext.text.toString()
                val encryptedText = encrypt(inputText) //
                binding.textviewEncryptes.text = encryptedText
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun decrypt(): String {
        return keyStoreUtils?.decrypt() ?: ("")
    }

    fun encrypt(data: String): String {
        val byteArray = data.encodeToByteArray() // to ByteArray
        return keyStoreUtils?.encrypt(byteArray)?.decodeToString() ?: ("")
    }
}
