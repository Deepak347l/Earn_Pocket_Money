package com.deepak.creactionforeveryone.Fragment.homeAssest.Model

data class Offer(
    val amount: String,
    val dating: Boolean,
    val description: String,
    val epc: String,
    val link: String,
    val mobile_app: Int,
    val mobile_app_icon_url: String,
    val mobile_app_id: String,
    val mobile_app_minimum_version: String,
    val mobile_app_type: String,
    val offerwall_only: Boolean,
    val payout_currency: String,
    val payout_type: String,
    val preview_url: String,
    val rank: Int,
    val ratio: String,
    val title: String,
    val traffic_type: String,
    val creatives: List<Creative>,
    val conversion: String,
    val campid: Int
)