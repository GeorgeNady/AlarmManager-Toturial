package com.george.alarmmanager.ui.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.george.alarmmanager.R
import com.george.alarmmanager.databinding.FragmentMainBinding
import com.george.alarmmanager.reciver.AlarmReceiver
import com.george.alarmmanager.util.BROADCAST_REQUEST_CODE
import com.george.alarmmanager.util.NOTIFICATION_ID
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val alarmManager by lazy { requireActivity().getSystemService(AlarmManager::class.java) }
    private val viewModel by viewModels<MainFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setListener()
    }

    private fun FragmentMainBinding.setListener() {
        lifecycleOwner = this@MainFragment
        bMainFragmentViewModel = viewModel

        // broadcast receiver
        val alarmBroadcastIntent = Intent(requireContext(), AlarmReceiver::class.java)
        val alarmPendingIntent = getBroadcast(
            requireContext(), BROADCAST_REQUEST_CODE, alarmBroadcastIntent, FLAG_IMMUTABLE)

        val alarmUp = getBroadcast(requireContext(), BROADCAST_REQUEST_CODE, alarmBroadcastIntent, FLAG_IMMUTABLE)
        switchMaterial.isChecked = alarmUp != null

        switchMaterial.setOnCheckedChangeListener { _, isChecked ->
            isChecked.onCheckedChanged()

            if (isChecked) {
                val repeatInterval: Long = 10 * 1000
                val elapsedRealtime = SystemClock.elapsedRealtime()
                val triggeredTime = elapsedRealtime + repeatInterval
                Log.d(this@MainFragment::class.simpleName, "repeatInterval: $repeatInterval")
                Log.d(this@MainFragment::class.simpleName, "elapsedRealtime: $elapsedRealtime")
                Log.d(this@MainFragment::class.simpleName, "elapsedRealtime dateFormat: ${dateFormat(elapsedRealtime)}")
                Log.d(this@MainFragment::class.simpleName, "triggeredTime: $triggeredTime")
                Log.d(this@MainFragment::class.simpleName, "triggeredTime dateFormat: ${dateFormat(triggeredTime)}")
                alarmManager?.let {
                    it.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        triggeredTime, repeatInterval, alarmPendingIntent
                    )
                    /*it.setAndAllowWhileIdle(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        triggeredTime, alarmPendingIntent
                    )*/
                    /*val alarmClockInfo = AlarmManager.AlarmClockInfo(triggeredTime, alarmPendingIntent)
                    it.setAlarmClock(alarmClockInfo, alarmPendingIntent)*/
                }
            } else {
                alarmManager?.cancel(alarmPendingIntent)
            }
        }

        btnNextAlarm.setOnClickListener {
            val nextAlarm = alarmManager.nextAlarmClock
            Toast.makeText(requireContext(), dateFormat(nextAlarm.triggerTime).trimIndent(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun Boolean.onCheckedChanged() {
        binding.textView.text =
            if (this) getString(R.string.active_alarm_label)
            else getString(R.string.inactive_alarm_label)

        val state = if (this) "on" else "off"
        Toast.makeText(requireContext(), "Alarm switched $state", Toast.LENGTH_SHORT).show()
    }

    private fun dateFormat(timeInMillis: Long):String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.getDefault())
        val calender = Calendar.getInstance()
        calender.timeInMillis = timeInMillis
        return formatter.format(calender.time)
    }

}