//package com.example.slaughterhousescreen.ui
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.animation.ObjectAnimator
//import android.annotation.SuppressLint
//import android.graphics.drawable.Drawable
//import android.media.MediaPlayer
//import android.net.Uri
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.WindowManager
//import android.view.animation.LinearInterpolator
//import android.widget.TextView
//import android.widget.Toast
//import android.widget.VideoView
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.viewpager2.widget.ViewPager2
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.signature.ObjectKey
//import com.example.slaughterhousescreen.R
//import com.example.slaughterhousescreen.data.CurrentQ
//import com.example.slaughterhousescreen.databinding.HomeFragmentBinding
//import com.example.slaughterhousescreen.util.PreferenceManager
//import com.example.slaughterhousescreen.viewmodel.CurrentTicketViewModel
//import com.example.slaughterhousescreen.viewmodel.GenericViewModelFactory
//import com.example.slaughterhousescreen.viewmodel.GetCurrentQViewModel
//import com.example.slaughterhousescreen.viewmodel.ScrollMessagesViewModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import com.bumptech.glide.request.target.Target
//import com.example.slaughterhousescreen.data.FileURL
//
//
//import com.example.slaughterhousescreen.viewmodel.CurrentTimeViewModel
//import com.example.slaughterhousescreen.viewmodel.GetImagesViewModel
//import com.example.slaughterhousescreen.viewmodel.ImagesAndVideosViewModel
//
//class HomeFragment : Fragment() {
//
//    private lateinit var binding: HomeFragmentBinding
//
//    private var isArabicAudioPlaying = false
//    private val ticketQueue: MutableList<Pair<String?, Int?>> = mutableListOf()
//
//    private var isEnglishAudioPlaying = false
//    private val englishTicketQueue: MutableList<Pair<String?, Int?>> = mutableListOf()
//    private var isEnglishAndArabicAudioPlaying = false
//
//    private var language: String? = null
//    var currentQAdapter: TicketAdapter? = null
//    var branchCode: String? = null
//    // var displayNumber: String? = null
//
//    private var mediaPlayer: MediaPlayer? = null // Declare a MediaPlayer variable
//    private val audioQueue: MutableList<Int> = mutableListOf()
//
//    private val englishAudioQueue: MutableList<Int> = mutableListOf()
//
//    private val handler = Handler()
//    private val refreshInterval = 5000L // 5 seconds
//
//
//    private val scrollMsgsHandler = Handler()
//    private val scrollMsgsRefreshInterval = 600000L // 10 minutes
//
//    private lateinit var englishTextView: TextView
//    private lateinit var arabicTextView: TextView
//
//
//    private val runnable = object : Runnable {
//        override fun run() {
//            callGetCurrentQApi()
//
//            if (!isEnglishAndArabicAudioPlaying && !isArabicAudioPlaying && !isEnglishAudioPlaying) {
//                // Skip API calls if  audio is playing
//                callCurrentTicketApi()
//            }
//
//            handler.postDelayed(this, refreshInterval) // Schedule next execution
//            //   screenHandler.postDelayed(this, screenRefreshInterval) // Schedule next execution
//
//        }
//    }
//
//    private val scrollMsgsRunnable = object : Runnable {
//        override fun run() {
//            callGetScrollMsgsApi() // Call the API every 10 minutes
//            scrollMsgsHandler.postDelayed(
//                this,
//                scrollMsgsRefreshInterval
//            ) // Schedule next execution
//        }
//    }
//
//    private val timeRefreshHandler = Handler()
//    private val timeRefreshInterval = 60000L // 1 minute (60,000 milliseconds)
//
//    // New runnable for refreshing the current time API
//    private val timeRefreshRunnable = object : Runnable {
//        override fun run() {
//            callCurrentTimeApi() // Call the current time API every 1 minute
//            timeRefreshHandler.postDelayed(this, timeRefreshInterval) // Schedule next execution
//        }
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = HomeFragmentBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        callCurrentTicketApi()
//        observerCurrentTicketViewModel()
//        callGetCurrentQApi()
//        observerCallCurrentQApi()
//        callGetScrollMsgsApi()
//        observerScrollMsgsViewModel()
//        }
//
//
//
//    private fun callCurrentTicketApi() {
//
//        if (!isEnglishAndArabicAudioPlaying && !isArabicAudioPlaying && !isEnglishAudioPlaying) {
//
//            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
//                currentTicketViewModel.getCurrentTicket(
//                    branchCode ?: "", "1"
//                    //    ,displayNumber?:""
//                )
//            }
//        } else {
//            Log.d("APIRequest", "Waiting for audio playback to finish before calling API.")
//        }
//    }
//
//
//    @SuppressLint("SetTextI18n")
//    private fun observerCurrentTicketViewModel() {
//        currentTicketViewModel.currentTicket.observe(viewLifecycleOwner) { currentTicket ->
//
//            val ticketNumberAudio = currentTicket.TicketNo
//            val counterIdAudio = currentTicket.CounterId?.toInt()
//
//            when (language) {
//                "1" -> {
//                    // Play only English audio
//                    enqueueEnglishTicket(ticketNumberAudio, counterIdAudio)
//                    //    playEnglishAudio(ticketNumberAudio, counterIdAudio)
//                }
//
//                "2" -> {
//                    // Play only Arabic audio
//                    enqueueTicket(ticketNumberAudio, counterIdAudio)
//                    // playArabicAudio(ticketNumberAudio, counterIdAudio)
//                }
//
//                "3" -> {
//                    enqueueEnglishTicket(ticketNumberAudio, counterIdAudio)
//                }
//
//
//                else -> {
//                    Log.e("AudioError", "Invalid language value: $language")
//                }
//            }
//
//        }
//    }
//
//    private fun enqueueEnglishTicket(ticketNumberAudio: String?, counterIdAudio: Int?) {
//        if (ticketNumberAudio != null && counterIdAudio != null) {
//            // Add the ticket to the queue
//            englishTicketQueue.add(Pair(ticketNumberAudio, counterIdAudio))
//
//            // Update UI to show the ticket number being processed
//            binding.titleCurrent.text = "$ticketNumberAudio" // Display ticket number
//            flashText(binding.titleCurrent, "$ticketNumberAudio ")
//
//            // Start playing the first audio if not already playing
//            if (!isEnglishAudioPlaying && !isEnglishAndArabicAudioPlaying) {
//                playNextAudioEnglish()
//            }
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun processNextEnglishTicket() {
//        if (englishTicketQueue.isNotEmpty() && !isEnglishAudioPlaying && !isEnglishAndArabicAudioPlaying)  {
//            val (ticketNumberAudio, counterIdAudio) = englishTicketQueue.removeAt(0)
//            binding.noCurrent.text = "Counter: $counterIdAudio"
//
//            playEnglishAudio(ticketNumberAudio, counterIdAudio)
//        } else {
//            Log.d("Queue", "No more tickets in queue.")
//        }
//    }
//
//
//    private fun enqueueTicket(ticketNumberAudio: String?, counterIdAudio: Int?) {
//        if (ticketNumberAudio != null && counterIdAudio != null) {
//            // Add the ticket to the queue
//            ticketQueue.add(Pair(ticketNumberAudio, counterIdAudio))
//
//            // Update UI to show the ticket number being processed
//            binding.titleCurrent.text = "$ticketNumberAudio" // Display ticket number
//            flashText(binding.titleCurrent, "$ticketNumberAudio ")
//
//            // Start playing the first audio if not already playing
//            if (!isArabicAudioPlaying ) {
//                playNextAudio()
//            }
//        }
//    }
//
//    private fun processNextTicket() {
//        if (ticketQueue.isNotEmpty() && !isArabicAudioPlaying ) {
//            val (ticketNumberAudio, counterIdAudio) = ticketQueue.removeAt(0)
//            binding.noCurrent.text = "Counter: $counterIdAudio"
//
//            playArabicAudio(ticketNumberAudio, counterIdAudio)
//        } else {
//            Log.d("Queue", "No more tickets in queue.")
//        }
//    }
//
//
//
//    @SuppressLint("DiscouragedApi")
//    private fun playEnglishAudio(
//        ticketNumberAudio: String?,
//        counterIdAudio: Int?,
//        onFinish: (() -> Unit)? = null
//    ) {
//        val firstCharEn = ticketNumberAudio?.firstOrNull()?.lowercaseChar()?.toString()
//        val ticketNumberWithoutPrefixEn = ticketNumberAudio?.substring(1)?.toInt()
//        Log.v("ticket number", firstCharEn + ticketNumberWithoutPrefixEn)
//
//
//        if (isEnglishAudioPlaying && isEnglishAndArabicAudioPlaying) {
//            Log.d("AudioPlayback", "Audio is already playing. Skipping API call.")
//            return
//        }
//
//        isEnglishAudioPlaying = true
//        isEnglishAndArabicAudioPlaying = true
//
//        //prepareEnglishAudioQueue(ticketNumberAudio, counterIdAudio)
//
//        englishAudioQueue.clear()
//        englishAudioQueue.add(R.raw.doorbell)
////        englishAudioQueue.add(R.raw.enticketnumber)
//
//        englishAudioQueue.add(R.raw.enticketnumber)
//        // ticket character
//        val ticketId =
//            resources.getIdentifier("en$firstCharEn", "raw", requireContext().packageName)
//        englishAudioQueue.add(ticketId)
//
//
//        // add ticket number
//        if (ticketNumberWithoutPrefixEn in 1..19) {
//            // Play the single audio file for numbers 1-19
//            val englishTicketAudioFileName = "en$ticketNumberWithoutPrefixEn"
//
//            val resourceId = resources.getIdentifier(
//                englishTicketAudioFileName,
//                "raw",
//                requireContext().packageName
//            )
//            Log.d(
//                "AudioResource",
//                "Resource ID for $englishTicketAudioFileName: $resourceId : $ticketNumberWithoutPrefixEn"
//            )
//
//            if (resourceId != 0) {
//                englishAudioQueue.add(resourceId)
//            } else {
//                Log.e("AudioError", "Resource not found for: $englishTicketAudioFileName")
//            }
//
//        } else if (ticketNumberWithoutPrefixEn in 20..99) {
//            // Split the number into tens and ones digits
//            val tens = ticketNumberWithoutPrefixEn?.div(10)  // e.g., 7 for 76
//            val ones = ticketNumberWithoutPrefixEn?.rem(10)  // e.g., 6 for 76
//
//            // Play the first audio for the tens digit (e.g., "en_70")
//            val englishTensAudioFileName = "en${tens?.times(10)}" // e.g., "en70"
//            val tensResourceId = resources.getIdentifier(
//                englishTensAudioFileName,
//                "raw",
//                requireContext().packageName
//            )
//
//            if (tensResourceId != 0) {
//                englishAudioQueue.add(tensResourceId)
//            } else {
//                println("English audio file not found for tens: $tens")
//            }
//
//            // Play the second audio for the ones digit if it's not 0 (e.g., "en_6")
//            if (ones != 0) {
//                val englishOnesAudioFileName = "en$ones" // e.g., "en_6"
//                val onesResourceId = resources.getIdentifier(
//                    englishOnesAudioFileName,
//                    "raw",
//                    requireContext().packageName
//                )
//
//                if (onesResourceId != 0) {
//                    englishAudioQueue.add(onesResourceId)
//                } else {
//                    println("English audio file not found for ones: $ones")
//                }
//            }
//
//        }
//
////        englishAudioQueue.add(R.raw.please) // Assuming audio2.mp3 is in res/raw
////        englishAudioQueue.add(R.raw.go) // Assuming audio2.mp3 is in res/raw
////        englishAudioQueue.add(R.raw.counter) // Assuming audio2.mp3 is in res/raw
//        englishAudioQueue.add(R.raw.enpleasegotocounter)
//
//
//        if (counterIdAudio != null) {
//            if (counterIdAudio < 100) {
//                val counterAudioFileName = "en$counterIdAudio"
//                val counterResourceId = resources.getIdentifier(
//                    counterAudioFileName,
//                    "raw",
//                    requireContext().packageName
//                )
//                englishAudioQueue.add(counterResourceId)
//            }
//
//        }
//
//        if (englishAudioQueue.isNotEmpty()) {
//            playNextAudioEnglish {
//                // This will trigger Arabic audio playback when English audio is finished
//                //  playArabicAudio(ticketNumberAudio, counterIdAudio)
//                if (language == "3") {
//                    enqueueTicket(ticketNumberAudio, counterIdAudio)
//                    //  playArabicAudio(ticketNumberAudio, counterIdAudio)
//                }
//            }
//        } else {
//            isEnglishAudioPlaying = false  // Reset flag if no audio is queued
//            isEnglishAndArabicAudioPlaying = false
//
//            processNextEnglishTicket()
//            // If no English audio is played, directly invoke onFinish
//        }
//    }
//
//    @SuppressLint("DiscouragedApi")
//    private fun playArabicAudio(ticketNumberAudio: String?, counterIdAudio: Int?) {
//        // Remove the first character (the "T") and get the number part
//        val firstChar = ticketNumberAudio?.firstOrNull()?.lowercaseChar()?.toString()
//        val ticketNumberWithoutPrefix = ticketNumberAudio?.substring(1)?.toInt()
//
//        if (isArabicAudioPlaying ) {
//            Log.d("AudioPlayback", "Audio is already playing. Skipping API call.")
//            return
//        }
//
//        isArabicAudioPlaying = true
//        //  isEnglishAndArabicAudioPlaying = true
//        //   prepareAudioQueue(ticketNumberAudio, counterIdAudio)
//
//
//        audioQueue.clear()
//        audioQueue.add(R.raw.doorbell)
//        audioQueue.add(R.raw.ticketar)
//        // audioQueue.add(R.raw.artkt)
//
//        // ticket character
//        val ticketId = resources.getIdentifier(firstChar, "raw", requireContext().packageName)
//        audioQueue.add(ticketId)
//
//
//        // add ticket number
//
//
//
//        // audioQueue.add(R.raw.collectionarea)
//
//        audioQueue.add(R.raw.arabic)
//
//        if (counterIdAudio != null) {
//            if (counterIdAudio < 100) {
//                val counterAudioFileName = "ar$counterIdAudio"
//                val counterResourceId = resources.getIdentifier(
//                    counterAudioFileName,
//                    "raw",
//                    requireContext().packageName
//                )
//                audioQueue.add(counterResourceId)
//            }
//        }
//
//        // Start playing the first audio if queue is not empty
//        if (audioQueue.isNotEmpty()) {
//            playNextAudio()
//        } else {
//            isArabicAudioPlaying = false  // Reset flag if no audio is queued
//            //  isEnglishAndArabicAudioPlaying = false
//
//            processNextTicket() // Move
//        }
//    }
//
//    private fun playNextAudioEnglish(onComplete: (() -> Unit)? = null) {
//        if (englishAudioQueue.isNotEmpty()) {
//            val resourceId = englishAudioQueue.removeAt(0)
//
//            if (resourceId != 0) {
//                mediaPlayer = MediaPlayer.create(requireContext(), resourceId)
//                mediaPlayer?.setOnCompletionListener {
//
//                    mediaPlayer = null
//                    playNextAudioEnglish(onComplete) // Recursively play the next audio
//                }
//                mediaPlayer?.start()
//            } else {
//                isEnglishAudioPlaying = false
//
//                Log.e("AudioError", "Invalid resource ID: $resourceId")
//                playNextAudioEnglish(onComplete) // Skip this invalid resource and continue to the next one
//            }
//        } else {
//            isEnglishAudioPlaying = false  // Reset flag to indicate no audio is playing
//            isEnglishAndArabicAudioPlaying = false
//
//            processNextEnglishTicket()
//
//            // All English audio has been played, now call the onComplete callback
//            onComplete?.invoke()
//        }
//    }
//
//    private fun playNextAudio(onComplete: (() -> Unit)? = null) {
//        if (audioQueue.isNotEmpty()) {
//            val resourceId = audioQueue.removeAt(0)
//
//            if (resourceId != 0) {
//                mediaPlayer = MediaPlayer.create(requireContext(), resourceId)
//
//                mediaPlayer?.apply {
//                    setOnCompletionListener {
//                        // Release the media player and continue with the next audio
//                        release()
//                        mediaPlayer = null
//
//                        // Continue playing the next audio in the queue
//                        playNextAudio(onComplete)
//                    }
//                    start()
//                }
//            } else {
//                Log.e("AudioError", "Invalid resource ID: $resourceId")
//                // Skip invalid file and continue with the next one
//                playNextAudio(onComplete)
//            }
//        } else {
//            // All audio in the queue has been played
//            isArabicAudioPlaying = false  // Reset flag to indicate no audio is playing
//            //  isEnglishAndArabicAudioPlaying = false
//
//            Log.d("AudioPlayback", "Arabic audio finished. API can be called now.")
//
//            // Process the next ticket
//            processNextTicket()
//
//            // Invoke the callback if provided
//            onComplete?.invoke()
//        }
//    }
//
//
//    private fun observerCallCurrentQApi() {
//        currentQViewModel.getCurrentQResponse.observe((viewLifecycleOwner)) { currentQList ->
//            currentQAdapter(currentQList)
//
//        }
//
//        currentQViewModel.errorResponse.observe(viewLifecycleOwner) {
//            // Toast.makeText(requireContext(), "get currentQ List Api failed", Toast.LENGTH_SHORT).show()
//        }
//
//    }
//
//    private fun callGetCurrentQApi() {
//        CoroutineScope(Dispatchers.IO).launch {
//            currentQViewModel.getCurrentQ(branchCode ?: "")
//        }
//    }
//
//    private fun observerScrollMsgsViewModel() {
//        scrollMessagesViewModel.getMsgsResponse.observe(viewLifecycleOwner) { scrollMsgs ->
//            //    binding.arabicText.text= "اهلا وسهلا بكم في المركز"
//            binding.arabicText.text = scrollMsgs?.ScrollMessageAr
//            binding.englishText.text = scrollMsgs?.ScrollMessageEn
//
//            setupMarquee(arabicTextView, true, 55000L)
//            setupMarquee(englishTextView, false, 45000L)
//        }
//
//        scrollMessagesViewModel.errorResponse.observe(viewLifecycleOwner) {
//            Toast.makeText(
//                requireContext(),
//                "please check your network or the entered info \"Base Url or branch code\"",
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }
//
//    }
//
//    private fun callGetScrollMsgsApi() {
//        CoroutineScope(Dispatchers.IO).launch {
//            scrollMessagesViewModel.getScrollMsgs(branchCode ?: "")
//
//        }
//
//    }
//
//    fun currentQAdapter(currentQList: List<CurrentQ?>) {
//
//        currentQAdapter = TicketAdapter(currentQList)
//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerView.adapter = currentQAdapter
//
//    }
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        audioQueue.clear() // Clear the audio queue
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//        handler.removeCallbacks(runnable)
//        timeRefreshHandler.removeCallbacks(timeRefreshRunnable) // Stop time refresh
//        // Stop the auto-refresh when fragment is not visible
//        //    screenHandler.removeCallbacks(runnable) // Stop the auto-refresh when fragment is not visible
//
//        scrollMsgsHandler.removeCallbacks(scrollMsgsRunnable) // Stop the scroll messages refresh
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        // Remove any pending callbacks to avoid memory leaks
//        //    timehandler.removeCallbacksAndMessages(null)
//        scrollMsgsHandler.removeCallbacksAndMessages(null)
//
//    }
//}