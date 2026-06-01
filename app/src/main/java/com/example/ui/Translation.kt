package com.example.ui

object Translation {
    val languages = listOf(
        LanguageOption("bn", "বাঙলা (Bangla)"),
        LanguageOption("en", "English"),
        LanguageOption("tl", "Tagalog"),
        LanguageOption("ta", "தமிழ் (Tamil)")
    )

    data class LanguageOption(val code: String, val name: String)

    private val translations = mapOf(
        // Tabs
        "tab_dashboard" to mapOf(
            "bn" to "ড্যাশবোর্ড",
            "en" to "Dashboard",
            "tl" to "Dashboard",
            "ta" to "டாஷ்போர்டு"
        ),
        "tab_transactions" to mapOf(
            "bn" to "লেনদেন",
            "en" to "Transactions",
            "tl" to "Transaksyon",
            "ta" to "பரிவர்த்தனைகள்"
        ),
        "tab_wallets" to mapOf(
            "bn" to "ওয়ালেট",
            "en" to "Wallets",
            "tl" to "Mga Wallet",
            "ta" to "வாலட்டுகள்"
        ),
        "tab_settings" to mapOf(
            "bn" to "বাজেট ও লক",
            "en" to "Settings & Lock",
            "tl" to "Settings & Lock",
            "ta" to "அமைப்புகள் & பூட்டு"
        ),

        // General / Header
        "app_title" to mapOf(
            "bn" to "হিসাব নিকাশ",
            "en" to "Hisab Nikash",
            "tl" to "Hisab Nikash",
            "ta" to "ஹிசாப் நிகாஷ்"
        ),
        "app_desc" to mapOf(
            "bn" to "আপনার ব্যক্তিগত ডিজিটাল ক্যাশ বুক",
            "en" to "Your personal digital cash book",
            "tl" to "Ang iyong personal na digital cash book",
            "ta" to "உங்கள் தனிப்பட்ட டிஜிட்டல் பண புத்தகம்"
        ),
        "total_balance" to mapOf(
            "bn" to "সর্বমোট ব্যালেন্স",
            "en" to "Total Balance",
            "tl" to "Kabuuang Balanse",
            "ta" to "மொத்த இருப்பு"
        ),
        "total_income" to mapOf(
            "bn" to "সর্বমোট আয়",
            "en" to "Total Income",
            "tl" to "Kabuuang Kita",
            "ta" to "மொத்த வருமானம்"
        ),
        "total_expense" to mapOf(
            "bn" to "সর্বমোট ব্যয়",
            "en" to "Total Expense",
            "tl" to "Kabuuang Gastos",
            "ta" to "மொத்த செலவு"
        ),
        "add_transaction" to mapOf(
            "bn" to "নতুন লেনদেন",
            "en" to "Add Transaction",
            "tl" to "Idagdag ang Transaksyon",
            "ta" to "பரிவர்த்தனை சேர்க்கவும்"
        ),
        "search_placeholder" to mapOf(
            "bn" to "লেনদেন অনুসন্ধান করুন...",
            "en" to "Search transactions...",
            "tl" to "Maghanap ng transaksyon...",
            "ta" to "பரிவர்த்தனைகளைத் தேடுக..."
        ),
        "no_transactions" to mapOf(
            "bn" to "কোনো লেনদেন পাওয়া যায়নি।",
            "en" to "No transactions found.",
            "tl" to "Walang nahanap na transaksyon.",
            "ta" to "பரிவர்த்தனைகள் எதுவும் இல்லை."
        ),
        "recent_transactions" to mapOf(
            "bn" to "সাম্প্রতিক লেনদেনসমূহ",
            "en" to "Recent Transactions",
            "tl" to "Mga Kamakailang Transaksyon",
            "ta" to "சமீபத்திய பரிவர்த்தனைகள்"
        ),

        // Lock Screen
        "app_secured" to mapOf(
            "bn" to "হিসাব নিকাশ সুরক্ষিত",
            "en" to "Hisab Nikash Secured",
            "tl" to "Hisab Nikash Secured",
            "ta" to "ஹிசாப் நிகாஷ் பாதுகாக்கப்பட்டது"
        ),
        "set_new_pin" to mapOf(
            "bn" to "নতুন সিকিউরিটি পিন সেট করুন",
            "en" to "Set New Security PIN",
            "tl" to "Magtakda ng Bagong PIN ng Seguridad",
            "ta" to "புதிய பாதுகாப்பு பினை அமைக்கவும்"
        ),
        "enter_4_digit_pin" to mapOf(
            "bn" to "আপনার ৪ ডিজিটের পিন কোডটি লিখুন",
            "en" to "Enter your 4-digit PIN",
            "tl" to "Ipasok ang iyong 4-digit na PIN",
            "ta" to "உங்கள் 4 இலக்க பின்னை உள்ளிடவும்"
        ),
        "secure_app_4_digit" to mapOf(
            "bn" to "অ্যাপটি লক করতে ৪ ডিজিটের কোড দিন",
            "en" to "Provide a 4-digit code to lock the app",
            "tl" to "Magbigay ng 4-digit na code para i-lock ang app",
            "ta" to "பயன்பாட்டைப் பூட்ட 4 இலக்க குறியீட்டை வழங்கவும்"
        ),
        "wrong_pin" to mapOf(
            "bn" to "ভুল পিন নম্বর! আবার চেষ্টা করুন।",
            "en" to "Incorrect PIN! Please try again.",
            "tl" to "Maling PIN! Subukan muli.",
            "ta" to "தவறான பின்! மீண்டும் முயற்சிக்கவும்."
        ),

        // Wallets
        "add_new_wallet" to mapOf(
            "bn" to "নতুন ওয়ালেট তৈরি করুন",
            "en" to "Create New Wallet",
            "tl" to "Gumawa ng Bagong Wallet",
            "ta" to "புதிய வாலட்டை உருவாக்கவும்"
        ),
        "active_accounts" to mapOf(
            "bn" to "সক্রিয় অ্যাকাউন্টসমূহ",
            "en" to "Active Accounts",
            "tl" to "Mga Aktibong Account",
            "ta" to "செயலில் உள்ள கணக்குகள்"
        ),
        "money_transfer" to mapOf(
            "bn" to "টাকা ট্রান্সফার বা স্থানান্তর",
            "en" to "Money Transfer",
            "tl" to "Paglipat ng Pera",
            "ta" to "பணப் பரிமாற்றம்"
        ),
        "transfer_from" to mapOf(
            "bn" to "যে ওয়ালেট থেকে পাঠাবেন",
            "en" to "Transfer from",
            "tl" to "Ipadala mula sa",
            "ta" to "இதிலிருந்து மாற்றவும்"
        ),
        "transfer_to" to mapOf(
            "bn" to "যে ওয়ালেটে পাঠাবেন",
            "en" to "Transfer to",
            "tl" to "Ipadala sa",
            "ta" to "இதற்கு மாற்றவும்"
        ),
        "transfer_notes" to mapOf(
            "bn" to "ট্রান্সফার নোট",
            "en" to "Transfer Notes",
            "tl" to "Mga Tala sa Paglipat",
            "ta" to "பரிமாற்றக் குறிப்புகள்"
        ),
        "transfer_button" to mapOf(
            "bn" to "টাকা স্থানান্তর করুন",
            "en" to "Transfer Funds",
            "tl" to "Maglipat ng Pondo",
            "ta" to "பணத்தை மாற்றவும்"
        ),
        "wallet_name" to mapOf(
            "bn" to "মালিকানা/ওয়ালেটের নাম",
            "en" to "Wallet Name",
            "tl" to "Pangalan ng Wallet",
            "ta" to "வாலட்டின் பெயர்"
        ),
        "wallet_balance" to mapOf(
            "bn" to "ব্যালেন্স পরিমাণ (৳)",
            "en" to "Balance Amount (৳)",
            "tl" to "Halaga ng Balanse (৳)",
            "ta" to "இருப்புத் தொகை (৳)"
        ),
        "wallet_type" to mapOf(
            "bn" to "অ্যাকাউন্ট ক্যাটাগরি ধরন",
            "en" to "Account Category Type",
            "tl" to "Uri ng Kategorya ng Account",
            "ta" to "கணக்கு வகை"
        ),
        "create_wallet" to mapOf(
            "bn" to "ওয়ালেট তৈরি করুন",
            "en" to "Create Wallet",
            "tl" to "Gumawa ng Wallet",
            "ta" to "வாலட்டை உருவாக்கவும்"
        ),
        "edit_wallet" to mapOf(
            "bn" to "💼 অ্যাকাউন্ট/ওয়ালেট সম্পাদনা করুন",
            "en" to "💼 Edit Account/Wallet",
            "tl" to "💼 I-edit ang Account/Wallet",
            "ta" to "💼 கணக்கு/வாலட்டைத் திருத்து"
        ),
        "update_wallet" to mapOf(
            "bn" to "আপডেট করুন",
            "en" to "Update",
            "tl" to "I-update",
            "ta" to "புதுப்பிக்கவும்"
        ),

        // Transactions Input / Dialog
        "expense" to mapOf(
            "bn" to "খরচ",
            "en" to "Expense",
            "tl" to "Gastos",
            "ta" to "செலவு"
        ),
        "income" to mapOf(
            "bn" to "আয়",
            "en" to "Income",
            "tl" to "Kita",
            "ta" to "வருமானம்"
        ),
        "edit_transaction" to mapOf(
            "bn" to "✍️ লেনদেন সম্পাদনা করুন",
            "en" to "✍️ Edit Transaction",
            "tl" to "✍️ I-edit ang Transaksyon",
            "ta" to "✍️ பரிவர்த்தனையைத் திருத்து"
        ),
        "amount" to mapOf(
            "bn" to "পরিমাণ (৳)",
            "en" to "Amount (৳)",
            "tl" to "Halaga (৳)",
            "ta" to "தொகை (৳)"
        ),
        "desc" to mapOf(
            "bn" to "লেনদেনের বিবরণ",
            "en" to "Description",
            "tl" to "Paglalarawan",
            "ta" to "விளக்கம்"
        ),
        "category" to mapOf(
            "bn" to "লেনদেনের খাত (Category)",
            "en" to "Category",
            "tl" to "Kategorya",
            "ta" to "வகை"
        ),
        "wallet" to mapOf(
            "bn" to "যে ওয়ালেট থেকে পরিশোধ/জমা হবে",
            "en" to "Wallet",
            "tl" to "Wallet",
            "ta" to "வாலட்"
        ),
        "notes" to mapOf(
            "bn" to "অতিরিক্ত নোট",
            "en" to "Notes",
            "tl" to "Mga Tala",
            "ta" to "குறிப்புகள்"
        ),
        "cancel" to mapOf(
            "bn" to "বাতিল",
            "en" to "Cancel",
            "tl" to "Kanselahin",
            "ta" to "ரத்துசெய்"
        ),
        "save" to mapOf(
            "bn" to "সংরক্ষণ",
            "en" to "Save",
            "tl" to "I-save",
            "ta" to "சேமி"
        ),

        // Budget Settings
        "budget_settings_title" to mapOf(
            "bn" to "বাজেট প্ল্যান ও অ্যাপ সেটিংস",
            "en" to "Budget Plan & App Settings",
            "tl" to "Planong Badyet at Mga Setting ng App",
            "ta" to "பட்ஜெட் திட்டம் & பயன்பாட்டு அமைப்புகள்"
        ),
        "budget_settings_desc" to mapOf(
            "bn" to "খরচের মাসিক খাতের সীমা এবং নিরাপত্তামূলক সেটিংস",
            "en" to "Monthly limits on expense categories and security settings",
            "tl" to "Buwanang limitasyon sa gastos at mga setting ng seguridad",
            "ta" to "செலவு பிரிவுகள் மற்றும் பாதுகாப்பு அமைப்புகளில் மாதாந்திர வரம்புகள்"
        ),
        "set_budget" to mapOf(
            "bn" to "🎯 খাতের বাজেট সেট করুন (Monthly Budget)",
            "en" to "🎯 Set Category Budget (Monthly Budget)",
            "tl" to "🎯 Magtakda ng Badyet ng Kategorya (Buwanang Badyet)",
            "ta" to "🎯 வகையின் பட்ஜெட்டை அமைக்கவும் (மாதாந்திர பட்ஜெட்)"
        ),
        "budget_category" to mapOf(
            "bn" to "বাজেট খাত: ",
            "en" to "Budget Category: ",
            "tl" to "Kategorya ng Badyet: ",
            "ta" to "பட்ஜெட் வகை: "
        ),
        "max_budget_amount" to mapOf(
            "bn" to "সর্বোচ্চ বাজেট পরিমাণ (৳)",
            "en" to "Maximum Budget Amount (৳)",
            "tl" to "Pinakamataas na Halaga ng Badyet (৳)",
            "ta" to "அதிகபட்ச பட்ஜெட் தொகை (৳)"
        ),
        "save_budget" to mapOf(
            "bn" to "বাজেট সীমা সংরক্ষণ করুন",
            "en" to "Save Budget Limit",
            "tl" to "I-save ang Limitasyon ng Badyet",
            "ta" to "பட்ஜெட் வரம்பைச் சேமிக்கவும்"
        ),
        "active_budgets" to mapOf(
            "bn" to "বিদ্যমান বাজেট সীমাসমূহ",
            "en" to "Active Budget Limits",
            "tl" to "Mga Aktibong Limitasyon sa Badyet",
            "ta" to "செயலில் உள்ள பட்ஜெட் வரம்புகள்"
        ),
        "no_budgets" to mapOf(
            "bn" to "কোনো বিশেষ বাজেট লক্ষ্য নির্ধারণ করা হয়নি।",
            "en" to "No specific budget target has been set.",
            "tl" to "Walang tinukoy na target na badyet na naitakda.",
            "ta" to "குறிப்பிட்ட பட்ஜெட் இலக்கு எதுவும் அமைக்கப்படவில்லை."
        ),
        "budget_spent" to mapOf(
            "bn" to "খরচ হয়েছে",
            "en" to "Spent",
            "tl" to "Nagastos",
            "ta" to "செலவிடப்பட்டது"
        ),
        "budget_limit" to mapOf(
            "bn" to "বাজেট",
            "en" to "Budget",
            "tl" to "Budget",
            "ta" to "பட்ஜெட்"
        ),
        "budget_exceeded" to mapOf(
            "bn" to "⚠️ আপনার এই খাতের খরচ বাজেট অতিক্রম করেছে!",
            "en" to "⚠️ Your expense in this category has exceeded the budget!",
            "tl" to "⚠️ Ang iyong gastos sa kategoryang ito ay lumampas sa badyet!",
            "ta" to "⚠️ இந்த பிரிவில் உங்கள் செலவு பட்ஜெட்டை தாண்டியது!"
        ),
        "budget_close" to mapOf(
            "bn" to "⚠️ বাজেট সীমার কাছাকাছি পৌঁছেছেন!",
            "en" to "⚠️ Getting close to the budget limit!",
            "tl" to "⚠️ Papalapit na sa limitasyon ng badyet!",
            "ta" to "⚠️ பட்ஜெட் வரம்பை நெருங்குகிறது!"
        ),
        "edit_budget" to mapOf(
            "bn" to "🎯 বাজেট পরিবর্তন করুন",
            "en" to "🎯 Edit Budget Limit",
            "tl" to "🎯 Baguhin ang Limitasyon ng Badyet",
            "ta" to "🎯 பட்ஜெட் வரம்பைத் திருத்து"
        ),
        "new_budget_amount" to mapOf(
            "bn" to "নতুন বাজেট সীমা পরিমাণ (৳)",
            "en" to "New Budget Limit Amount (৳)",
            "tl" to "Bagong Halaga ng Limitasyon sa Badyet (৳)",
            "ta" to "புதிய பட்ஜெட் வரம்புத் தொகை (৳)"
        ),
        "change_budget" to mapOf(
            "bn" to "পরিবর্তন করুন",
            "en" to "Change",
            "tl" to "Palitan",
            "ta" to "மாற்றவும்"
        ),

        // Security PIN
        "security_lock_title" to mapOf(
            "bn" to "🔒 সিকিউরিটি লক (PIN System)",
            "en" to "🔒 Security Lock (PIN System)",
            "tl" to "🔒 Lock ng Seguridad (Sistema ng PIN)",
            "ta" to "🔒 பாதுகாப்பு பூட்டு (பின் முறை)"
        ),
        "security_lock_desc_active" to mapOf(
            "bn" to "সিকিউরিটি লক বর্তমানে সক্রিয় আছে।" ,
            "en" to "Security lock is currently active.",
            "tl" to "Kasalukuyang aktibo ang lock ng seguridad.",
            "ta" to "பாதுகாப்பு பூட்டு தற்போது செயலில் உள்ளது."
        ),
        "security_lock_desc_inactive" to mapOf(
            "bn" to "লেনদেন সুরক্ষিত রাখতে সিকিউরিটি ৪ ডিজিট কোড লক চালু করতে পারবেন।",
            "en" to "You can enable a 4-digit code lock to keep transactions secure.",
            "tl" to "Maaari mong paganahin ang isang 4-digit na lock ng code upang mapanatiling ligtas ang mga transaksyon.",
            "ta" to "பரிவர்த்தனைகளை பாதுகாப்பாக வைக்க 4 இலக்க குறியீட்டு பூட்டை நீங்கள் இயக்கலாம்."
        ),
        "new_pin_label" to mapOf(
            "bn" to "নতুন চার ডিজিটের পিন (Digits Only)",
            "en" to "New 4-digit PIN (Digits Only)",
            "tl" to "Bagong 4-digit na PIN (Mga Numero Lang)",
            "ta" to "புதிய 4 இலக்க பின் (எண்கள் மட்டும்)"
        ),
        "enable_pin_btn" to mapOf(
            "bn" to "পিন লক অ্যাক্টিভ করুন",
            "en" to "Activate PIN Lock",
            "tl" to "I-activate ang Lock ng PIN",
            "ta" to "பினைச் செயல்படுத்தவும்"
        ),
        "disable_pin_btn" to mapOf(
            "bn" to "পিন লক ডিসেবল করুন",
            "en" to "Disable PIN Lock",
            "tl" to "I-disable ang Lock ng PIN",
            "ta" to "பினை முடக்கவும்"
        ),

        // Export / CSV
        "export_title" to mapOf(
            "bn" to "এক্সেল বা সিএসভি রিপোর্ট খতিয়ান",
            "en" to "Excel or CSV Ledger Report",
            "tl" to "Ulat ng Excel o CSV Ledger",
            "ta" to "எக்செல் அல்லது சிஎஸ்வி லெட்ஜர் அறிக்கை"
        ),
        "export_desc" to mapOf(
            "bn" to "আপনার আয়ের খাত ও ব্যয়ের চূড়ান্ত খতিয়ান পিডিএফ বা Excel (CSV) শীট হিসাবে এক্সপোর্ট বা ব্যাকআপ করুন।",
            "en" to "Export or backup your final income and expense ledger as a PDF or Excel (CSV) sheet.",
            "tl" to "I-export o i-back up ang iyong huling libro ng kita at gastos bilang PDF o Excel (CSV) sheet.",
            "ta" to "உங்கள் இறுதி வருமானம் மற்றும் செலவு லெட்ஜரை PDF அல்லது Excel (CSV) தாளாக ஏற்றுமதி அல்லது காப்புப்பிரதி எடுக்கவும்."
        ),
        "export_btn" to mapOf(
            "bn" to "সব হিসাব শেয়ার বা ডাওনলোড করুন",
            "en" to "Share or Download All Accounts",
            "tl" to "Ibahagi o I-download ang Lahat ng Account",
            "ta" to "அனைத்து கணக்குகளையும் பகிரவும் அல்லது பதிவிறக்கவும்"
        ),

        // Cloud Drive Backup
        "cloud_backup_title" to mapOf(
            "bn" to "☁️ গুগল ড্রাইভ ক্লাউড ব্যাকআপ",
            "en" to "☁️ Google Drive Cloud Backup",
            "tl" to "☁️ Google Drive Cloud Backup",
            "ta" to "☁️ கூகுள் டிரைவ் கிளவுட் காப்புப்பிரதி"
        ),
        "cloud_backup_desc" to mapOf(
            "bn" to "আপনার হিসাব নিকাশ ক্লাউডে সংরক্ষণ ও যেকোনো সময় বা অন্য ডিভাইসে রিস্টোর করুন নিরাপদে।",
            "en" to "Safely store your accounts in the cloud and restore them anytime or on other devices.",
            "tl" to "Ligtas na iimbak ang iyong mga account sa cloud at i-restore ang mga ito anumang oras o sa ibang mga device.",
            "ta" to "உங்கள் கணக்குகளை கிளவுடில் பாதுகாப்பாகச் சேமித்து, எப்போது வேண்டுமானாலும் அல்லது பிற சாதனங்களில் மீட்டெடுக்கவும்."
        ),
        "connect_google_btn" to mapOf(
            "bn" to "গুগল ড্রাইভ একাউন্ট যুক্ত করুন",
            "en" to "Connect Google Drive Account",
            "tl" to "I-connect ang Google Drive Account",
            "ta" to "கூகுள் டிரைவ் கணக்கை இணைக்கவும்"
        ),
        "signed_in_as" to mapOf(
            "bn" to "সংযুক্ত হিসাব:",
            "en" to "Connected Account:",
            "tl" to "Nakakonektang Account:",
            "ta" to "இணைக்கப்பட்ட கணக்கு:"
        ),
        "disconnect_btn" to mapOf(
            "bn" to "কানেকশন বিচ্ছিন্ন করুন",
            "en" to "Disconnect Account",
            "tl" to "I-disconnect ang Account",
            "ta" to "கணக்கைத் துண்டி"
        ),
        "last_backup_prefix" to mapOf(
            "bn" to "শেষ সফল ব্যাকআপ:",
            "en" to "Last Successful Backup:",
            "tl" to "Huling Matagumpay na Backup:",
            "ta" to "கடைசி வெற்றிகரமான காப்புப்பிரதி:"
        ),
        "backup_now_btn" to mapOf(
            "bn" to "এখনই ব্যাকআপ করুন 📤",
            "en" to "Backup Now 📤",
            "tl" to "I-backup Ngayon 📤",
            "ta" to "இப்போது காப்புப்பிரதி எடுக்கவும் 📤"
        ),
        "restore_now_btn" to mapOf(
            "bn" to "ব্যাকআপ থেকে রিস্টোর করুন 📥",
            "en" to "Restore from Backup 📥",
            "tl" to "I-restore mula sa Backup 📥",
            "ta" to "காப்புப்பிரதியிலிருந்து மீட்டெடுங்கள் 📥"
        ),

        // Settings Selector
        "app_language_title" to mapOf(
            "bn" to "🌐 ভাষা নির্বাচন করুন (App Language)",
            "en" to "🌐 Select App Language",
            "tl" to "🌐 Pumili ng Wika ng App",
            "ta" to "🌐 பயன்பாட்டு மொழியைத் தேர்ந்தெடுக்கவும்"
        ),

        // Categories translation
        "খাবার" to mapOf("bn" to "খাবার", "en" to "Food", "tl" to "Pagkain", "ta" to "உணவு"),
        "যাতায়াত" to mapOf("bn" to "যাতায়াত", "en" to "Transport", "tl" to "Transportasyon", "ta" to "போக்குவரத்து"),
        "বাড়ি ভাড়া" to mapOf("bn" to "বাড়ি ভাড়া", "en" to "Rent", "tl" to "Renta o Upa", "ta" to "வீட்டு வாடகை"),
        "ইউটিলিটি বিল" to mapOf("bn" to "ইউটিলিটি বিল", "en" to "Utilities", "tl" to "Mga Utility", "ta" to "பயன்பாட்டு பில்கள்"),
        "চিকিৎসা" to mapOf("bn" to "চিকিৎসা", "en" to "Medical", "tl" to "Medikal", "ta" to "மருத்துவம்"),
        "শিক্ষা" to mapOf("bn" to "শিক্ষা", "en" to "Education", "tl" to "Edukasyon", "ta" to "கல்வி"),
        "বিনোদন" to mapOf("bn" to "বিনোদন", "en" to "Entertainment", "tl" to "Libangan", "ta" to "பொழுதுபோக்கு"),
        "শপিং" to mapOf("bn" to "শপিং", "en" to "Shopping", "tl" to "Pamimili", "ta" to "ஷாப்பிங்"),
        "ঋণ পরিশোধ" to mapOf("bn" to "ঋণ পরিশোধ", "en" to "Debt Repayment", "tl" to "Pagbabayad ng Utang", "ta" to "கடன் திருப்பிச் செலுத்துதல்"),
        "অন্যান্য" to mapOf("bn" to "অন্যান্য", "en" to "Other / Miscellaneous", "tl" to "Iba Pa", "ta" to "மற்றவை"),

        "বেতন" to mapOf("bn" to "বেতন", "en" to "Salary", "tl" to "Sahod", "ta" to "சம்பளம்"),
        "ব্যবসা" to mapOf("bn" to "ব্যবসা", "en" to "Business", "tl" to "Negosyo", "ta" to "வணிகம்"),
        "ফ্রিল্যান্সিং" to mapOf("bn" to "ফ্রিল্যান্সিং", "en" to "Freelancing", "tl" to "Freelancing", "ta" to "ஃப்ரீலான்சிங்"),
        "উপহার" to mapOf("bn" to "উপহার", "en" to "Gift", "tl" to "Regalo", "ta" to "பரிசு"),

        "edit_balance_title" to mapOf(
            "bn" to "ব্যালেন্স সমন্বয় করুন",
            "en" to "Adjust Main Balance",
            "tl" to "I-adjust ang Kabuuang Balanse",
            "ta" to "முதன்மையான இருப்பைச் சரிசெய்"
        ),
        "adjust_balances_desc" to mapOf(
            "bn" to "ওয়ালেটের নতুন ব্যালেন্স টাইপ করে মোট ব্যালেন্স পরিবর্তন করতে পারেন",
            "en" to "Specify new balances for your accounts/wallets below to update the total sum",
            "tl" to "Tukuyin ang mga bagong balanse sa ibaba para ma-update ang kabuuang halaga",
            "ta" to "இருப்பு விவரங்களை மாற்றி அமைக்க வாலட்டுகளின் புதிய தொகையை உள்ளிடவும்"
        ),
        "save_adjustments_btn" to mapOf(
            "bn" to "ব্যালেন্স আপডেট করুন",
            "en" to "Update Balances",
            "tl" to "I-update ang mga Balanse",
            "ta" to "இருப்புகளைப் புதுப்பிக்கவும்"
        ),
        "err_no_wallets_edit" to mapOf(
            "bn" to "ব্যালেন্স পরিবর্তন করতে প্রথমে একটি ওয়ালেট তৈরি করুন।",
            "en" to "No wallets available to edit! Please create a wallet first under Wallets tab.",
            "tl" to "Walang mga wallet na mai-edit! Gumawa muna ng wallet sa tab ng Mga Wallet.",
            "ta" to "திருத்துவதற்கு போதுமான வாலட்டுகள் இல்லை! வாலட்டுகள் பிரிவில் வாலட்டை உருவாக்கவும்."
        ),

        // Missing App Subtitle & Home Analytics
        "app_subtitle" to mapOf(
            "bn" to "আপনার ব্যক্তিগত ডিজিটাল ক্যাশ বুক",
            "en" to "Your personal digital cash book",
            "tl" to "Ang iyong personal na digital cash book",
            "ta" to "உங்கள் தனிப்பட்ட டிஜிட்டல் பண புத்தகம்"
        ),
        "budget_exceeded_title" to mapOf(
            "bn" to "⚠️ বাজেট সীমা অতিক্রম করেছে!",
            "en" to "⚠️ Budget Limit Exceeded!",
            "tl" to "⚠️ Lumampas sa Limitasyon ng Badyet!",
            "ta" to "⚠️ பட்ஜெட் வரம்பு மீறப்பட்டது!"
        ),
        "budget_exceeded_msg" to mapOf(
            "bn" to "খাতে বাজেট ছাড়িয়ে গেছে। খরচ নিয়ন্ত্রণে রাখুন।",
            "en" to "category/categories exceeded the monthly budget. Keep an eye on expenses!",
            "tl" to "kategorya ay lumampas sa buwanang badyet. Bantayan ang mga gastos!",
            "ta" to "மாதாந்திர பட்ஜெட்டைத் தாண்டியது. செலவைக் கண்காணியுங்கள்!"
        ),
        "visual_analytics" to mapOf(
            "bn" to "দৃষ্টিনন্দন খরচ খতিয়ান (Analytics)",
            "en" to "Visual Spending Analytics",
            "tl" to "Visual na Pagsusuri ng Gastos",
            "ta" to "செலவு பகுப்பாய்வு விளக்கப்படம்"
        ),
        "no_expenses_logged" to mapOf(
            "bn" to "কোনো ব্যয়ের রেকর্ড নেই",
            "en" to "No expense records logged yet.",
            "tl" to "Wala pang naitalang gastos.",
            "ta" to "செலவுப் பதிவுகள் எதுவும் இல்லை."
        ),
        "add_expense_hint" to mapOf(
            "bn" to "খরচ করার সাথে সাথে তার খতিয়ান যুক্ত করুন।",
            "en" to "Add some expenses under Transactions tab to view analytical visual graphs here.",
            "tl" to "Magdagdag ng mga gastos sa ilalim ng tab ng Mga Transaksyon upang makita ang pagsusuri rito.",
            "ta" to "விளக்கப்படத்தைப் பார்க்க பரிவர்த்தனைகள் பிரிவில் செலவுகளைச் சேர்க்கவும்."
        ),
        "unknown_wallet" to mapOf(
            "bn" to "অজানা ওয়ালেট",
            "en" to "Unknown Wallet",
            "tl" to "Hindi Kilalang Wallet",
            "ta" to "அறியப்படாத வாலட்"
        ),

        // Transactions Record Tab
        "transactions_record" to mapOf(
            "bn" to "লেনদেনের খতিয়ান",
            "en" to "Transactions Registry",
            "tl" to "Listahan ng mga Transaksyon",
            "ta" to "பரிவர்த்தனை பதிவுகள்"
        ),
        "transactions_record_desc" to mapOf(
            "bn" to "আপনার আয়ের খাত ও ব্যয়ের চূড়ান্ত খতিয়ান অনুসন্ধান ও ফিল্টার করুন",
            "en" to "Search, filter, and review all your income and expenses safely",
            "tl" to "Maghanap, mag-filter, at suriin ang lahat ng iyong kita at gastos nang ligtas",
            "ta" to "வருமானம் மற்றும் செலவுகளைத் தேடவும், வடிகட்டவும், மதிப்பாய்வு செய்யவும்"
        ),
        "all_categories" to mapOf(
            "bn" to "সকল ক্যাটাগরি",
            "en" to "All Categories",
            "tl" to "Lahat ng Kategorya",
            "ta" to "அனைத்து வகைகள்"
        ),
        "all_wallets" to mapOf(
            "bn" to "সকল ওয়ালেট",
            "en" to "All Wallets",
            "tl" to "Lahat ng Wallet",
            "ta" to "அனைத்து வாலட்டுகள்"
        ),
        "no_search_results" to mapOf(
            "bn" to "অনুসন্ধানের কোনো ফলাফল পাওয়া যায়নি।",
            "en" to "No search matches found for the criteria.",
            "tl" to "Walang nahanap na tugma sa paghahanap.",
            "ta" to "தேடலுக்கான முடிவுகள் எதுவும் இல்லை."
        ),
        "note_label" to mapOf(
            "bn" to "নোট:",
            "en" to "Note:",
            "tl" to "Tala:",
            "ta" to "குறிப்பு:"
        ),

        // Wallets tab
        "wallets_title" to mapOf(
            "bn" to "আপনার ওয়ালেট ও ফান্ড ট্রান্সফার",
            "en" to "Your Wallets & Secure Transfers",
            "tl" to "Iyong mga Wallet at Ligtas na Paglilipat",
            "ta" to "வாலட்டுகள் & பணப் பரிமாற்றம்"
        ),
        "wallets_desc" to mapOf(
            "bn" to "আলাদা ওয়ালেট পরিচালনা করুন ও ওয়ালেটের ভেতর ব্যালেন্স স্থানান্তর করুন দ্রুত",
            "en" to "Manage multiple wallets and quickly transfer balance between them",
            "tl" to "Pamahalaan ang maramihang mga wallet at mabilis na maglipat ng balanse",
            "ta" to "பல வாலட்டுகளை நிர்வகித்து, அவற்றுக்கு இடையே பணத்தை மாற்றவும்"
        ),
        "no_wallets" to mapOf(
            "bn" to "কোনো সক্রিয় ওয়ালেট নেই।",
            "en" to "No active wallets available.",
            "tl" to "Walang magagamit na aktibong wallet.",
            "ta" to "செயலில் உள்ள வாலட்டுகள் எதுவும் இல்லை."
        ),
        "add_wallet" to mapOf(
            "bn" to "ওয়ালেট যোগ করুন",
            "en" to "Add Wallet",
            "tl" to "Idagdag ang Wallet",
            "ta" to "வாலட்டைச் சேர்க்கவும்"
        ),
        "fund_transfer_title" to mapOf(
            "bn" to "🔄 ওয়ালেট টু ওয়ালেট ফান্ড ট্রান্সফার",
            "en" to "🔄 Wallet-to-Wallet Fund Transfer",
            "tl" to "🔄 Paglipat ng Pondo sa pagitan ng mga Wallet",
            "ta" to "🔄 வாலட்டுகளுக்கிடையேயான பரிமாற்றம்"
        ),
        "transfer_from_placeholder" to mapOf(
            "bn" to "উৎস ওয়ালেট সিলেক্ট করুন",
            "en" to "Select Source Wallet",
            "tl" to "Pumili ng Pinagmulang Wallet",
            "ta" to "அனுப்பும் வாலட்டைத் தேர்வு செய்க"
        ),
        "transfer_to_placeholder" to mapOf(
            "bn" to "গন্তব্য ওয়ালেট সিলেক্ট করুন",
            "en" to "Select Destination Wallet",
            "tl" to "Pumili ng Pupuntahang Wallet",
            "ta" to "பெறும் வாலட்டைத் தேர்வு செய்க"
        ),
        "transfer_amount_label" to mapOf(
            "bn" to "স্থানান্তরের পরিমাণ (৳)",
            "en" to "Transfer Amount (৳)",
            "tl" to "Halaga ng Paglipat (৳)",
            "ta" to "மாற்ற வேண்டிய தொகை (৳)"
        ),
        "transfer_notes_label" to mapOf(
            "bn" to "विशेष মন্তব্য (ঐচ্ছিক)",
            "en" to "Transfer Notes (Optional)",
            "tl" to "Mga Tala sa Paglipat (Opsyonal)",
            "ta" to "பரிமாற்றக் குறிப்புகள் (விருப்பம்)"
        ),
        "err_select_wallets" to mapOf(
            "bn" to "অনুগ্রহ করে দুটি ওয়ালেটই সঠিকভাবে সিলেক্ট করুন।",
            "en" to "Please select both source and destination wallets.",
            "tl" to "Pumili ng kapwa pinagmulan at pupuntahang wallet.",
            "ta" to "அனுப்பும் மற்றும் பெறும் வாலட்டுகளைத் தேர்வு செய்க."
        ),
        "err_same_wallet" to mapOf(
            "bn" to "একই ওয়ালেটে টাকা স্থানান্তর সম্ভব নয়!",
            "en" to "Cannot transfer to the same wallet!",
            "tl" to "Hindi maaaring maglipat sa parehong wallet!",
            "ta" to "ஒரே வாலட்டுக்கு பணத்தை மாற்ற முடியாது!"
        ),
        "err_invalid_amount" to mapOf(
            "bn" to "অনুগ্রহ করে সঠিক স্থানান্তরের পরিমাণ লিখুন যা ওয়ালেটের ব্যালেন্সের চেয়ে বেশি নয়।",
            "en" to "Please enter a valid positive amount not exceeding source wallet's balance.",
            "tl" to "Magpasok ng wastong halaga na hindi hihigit sa balanse ng pinagmulang wallet.",
            "ta" to "அனுப்பும் வாலட்டின் இருப்புக்கு மிகாமல் சரியான தொகையை உள்ளிடவும்."
        ),
        "success_transfer" to mapOf(
            "bn" to "টাকা সফলভাবে স্থানান্তর হয়েছে!",
            "en" to "Funds transferred successfully!",
            "tl" to "Matagumpay na nailipat ang pondo!",
            "ta" to "பணம் வெற்றிகரமாக மாற்றப்பட்டது!"
        ),
        "transfer_confirm_btn" to mapOf(
            "bn" to "ট্রান্সফার চূড়ান্ত করুন",
            "en" to "Confirm & Execute Transfer",
            "tl" to "Kumpirmahin at Isagawa ang Paglipat",
            "ta" to "பரிமாற்றத்தை உறுதிப்படுத்துக"
        ),

        // Action dialog labels & alerts
        "title_add_tx" to mapOf(
            "bn" to "নতুন লেনদেন যুক্ত করুন",
            "en" to "Add Transaction Entry",
            "tl" to "Magdagdag ng Entry ng Transaksyon",
            "ta" to "பரிவர்த்தனையைச் சேர்க்கவும்"
        ),
        "tag_expense_btn" to mapOf(
            "bn" to "খরচ",
            "en" to "Expense",
            "tl" to "Gastos",
            "ta" to "செலவு"
        ),
        "tag_income_btn" to mapOf(
            "bn" to "আয়",
            "en" to "Income",
            "tl" to "Kita",
            "ta" to "வருமானம்"
        ),
        "amount_lbl" to mapOf(
            "bn" to "টাকার পরিমাণ (৳)",
            "en" to "Amount (৳)",
            "tl" to "Halaga (৳)",
            "ta" to "தொகை (৳)"
        ),
        "title_lbl" to mapOf(
            "bn" to "সংক্ষিপ্ত বিবরণ / উৎস / উদ্দেশ্য",
            "en" to "Short Title / Purpose",
            "tl" to "Maikling Pamagat / Layunin",
            "ta" to "சுருக்கமான தலைப்பு / நோக்கம்"
        ),
        "category_lbl" to mapOf(
            "bn" to "ক্যাটাগরি খাত",
            "en" to "Category Sector",
            "tl" to "Kategorya ng Sektor",
            "ta" to "பிரிவு"
        ),
        "associated_wallet_lbl" to mapOf(
            "bn" to "যুক্ত ওয়ালেট",
            "en" to "Associated Wallet",
            "tl" to "Kaugnay na Wallet",
            "ta" to "தொடர்புடைய வாலட்"
        ),
        "select_wallet_placeholder" to mapOf(
            "bn" to "ওয়ালেট নির্বাচন করুন",
            "en" to "Select Wallet",
            "tl" to "Pumili ng Wallet",
            "ta" to "வாலட்டைத் தேர்வு செய்க"
        ),
        "write_additional_notes_placeholder" to mapOf(
            "bn" to "অতিরিক্ত মন্তব্য লিখুন (ঐচ্ছিক)",
            "en" to "Write additional details (Optional)",
            "tl" to "Isulat ang karagdagang detalye (Opsyonal)",
            "ta" to "கூடுதல் குறிப்புகளை எழுதுக (விருப்பம்)"
        ),
        "cancel_close_btn" to mapOf(
            "bn" to "বাতিল করুন",
            "en" to "Cancel",
            "tl" to "Kanselahin",
            "ta" to "ரத்துசெய்"
        ),
        "err_log_amount" to mapOf(
            "bn" to "অনুগ্রহ করে সঠিক টাকার পরিমাণ দিন (ঋণাত্মক বা শূন্য নয়)।",
            "en" to "Please enter a valid positive amount.",
            "tl" to "Magpasok ng wastong positibong halaga.",
            "ta" to "பூஜ்ஜியத்திற்கு அதிகமான சரியான தொகையை உள்ளிடவும்."
        ),
        "err_associated_wallet_missing" to mapOf(
            "bn" to "দয়া করে একটি সংযুক্ত ওয়ালেট নির্বাচন করুন।",
            "en" to "Please select an associated wallet.",
            "tl" to "Pumili ng kaugnay na wallet.",
            "ta" to "தொடர்புடைய வாலட்டைத் தேர்வு செய்க."
        ),
        "success_added_record" to mapOf(
            "bn" to "লেনদেন সফলভাবে রেকর্ড করা হয়েছে!",
            "en" to "Transaction logged successfully!",
            "tl" to "Matagumpay na naitala ang transaksyon!",
            "ta" to "பரிவர்த்தனை வெற்றிகரமாகப் பதிவு செய்யப்பட்டது!"
        ),
        "save_record_btn" to mapOf(
            "bn" to "সংরক্ষণ করুন",
            "en" to "Save Record",
            "tl" to "I-save ang Record",
            "ta" to "பதிவைச் சேমি"
        ),

        // Wallet Creation & Editing
        "title_add_wallet" to mapOf(
            "bn" to "নতুন ওয়ালেট তৈরি করুন",
            "en" to "Create New Wallet",
            "tl" to "Gumawa ng Bagong Wallet",
            "ta" to "புதிய வாலட்டை உருவாக்கவும்"
        ),
        "wallet_name_lbl" to mapOf(
            "bn" to "ওয়ালেটের নাম (যেমন: নগদ, ব্যাংক হিসাব)",
            "en" to "Wallet Name (e.g., bKash, Bank Account)",
            "tl" to "Pangalan ng Wallet (hal., Cash, Bank)",
            "ta" to "வாலட்டின் பெயர் (எ.கா., ரொக்கம், வங்கி)"
        ),
        "initial_balance_lbl" to mapOf(
            "bn" to "প্রাথমিক ব্যালেন্স (৳)",
            "en" to "Initial Balance (৳)",
            "tl" to "Paunang Balanse (৳)",
            "ta" to "தொடக்க இருப்பு (৳)"
        ),
        "wallet_category_lbl" to mapOf(
            "bn" to "ওয়ালেটের ধরন",
            "en" to "Wallet Category Type",
            "tl" to "Uri ng Kategorya ng Wallet",
            "ta" to "வாலட்டின் வகை"
        ),
        "err_wallet_name_empty" to mapOf(
            "bn" to "ওয়ালেটের নাম খালি হতে পারে না!",
            "en" to "Wallet name cannot be empty!",
            "tl" to "Hindi pwedeng walang pangalan ang wallet!",
            "ta" to "வாலட்டின் பெயர் காலியாக இருக்கக் கூடாது!"
        ),
        "err_negative_balance" to mapOf(
            "bn" to "ব্যালেন্স ঋণাত্মক হতে পারে না!",
            "en" to "Balance cannot be negative!",
            "tl" to "Hindi pwedeng negatibo ang balanse!",
            "ta" to "இருப்பு எதிர்மறையாக இருக்கக் கூடாது!"
        ),
        "success_add_wallet" to mapOf(
            "bn" to "ওয়ালেট সফলভাবে তৈরি হয়েছে!",
            "en" to "Wallet created successfully!",
            "tl" to "Matagumpay na nagawa ang wallet!",
            "ta" to "வாலট வெற்றிகரமாக உருவாக்கப்பட்டது!"
        ),

        // Transaction Editing
        "title_edit_tx" to mapOf(
            "bn" to "লেনদেন সংশোধন করুন",
            "en" to "Edit Transaction Entry",
            "tl" to "I-edit ang Entry ng Transaksyon",
            "ta" to "பரிவர்த்தனையைத் திருத்து"
        ),
        "success_edit_record" to mapOf(
            "bn" to "লেনদেন সফলভাবে সংশোধন করা হয়েছে!",
            "en" to "Transaction updated successfully!",
            "tl" to "Matagumpay na na-update ang transaksyon!",
            "ta" to "பரிவர்த்தனை வெற்றிகரமாகப் புதுப்பிக்கப்பட்டது!"
        ),

        // Wallet Editing
        "title_edit_wallet" to mapOf(
            "bn" to "ওয়ালেট সংশোধন করুন",
            "en" to "Edit Wallet Details",
            "tl" to "I-edit ang mga Detalye ng Wallet",
            "ta" to "வாলட்டைத் திருத்து"
        ),
        "success_edit_wallet" to mapOf(
            "bn" to "ওয়ালেট সফলভাবে আপডেট করা হয়েছে!",
            "en" to "Wallet updated successfully!",
            "tl" to "Matagumpay na na-update ang wallet!",
            "ta" to "வாலট வெற்றிகரமாகப் புதுப்பிக்கப்பட்டது!"
        ),

        // Budget Editing
        "title_edit_budget" to mapOf(
            "bn" to "খাতের বাজেট পরিবর্তন করুন",
            "en" to "Adjust Category Budget",
            "tl" to "I-adjust ang Badyet ng Kategorya",
            "ta" to "பிரிவு பட்ஜெட்டை சரிசெய்"
        ),
        "new_budget_limit_lbl" to mapOf(
            "bn" to "নতুন বাজেট সীমা (৳)",
            "en" to "New Budget Limit Amount (৳)",
            "tl" to "Bagong Halaga ng Limitasyon sa Badyet (৳)",
            "ta" to "புதிய பட்ஜெட் வரம்புத் தொகை (৳)"
        ),
        "err_positive_budget" to mapOf(
            "bn" to "অনুগ্রহ করে সঠিক ইতিবাচক বাজেট পরিমাণ দিন।",
            "en" to "Please specify a positive budget limit.",
            "tl" to "Tukuyin ang positibong limitasyon ng badyet.",
            "ta" to "பூஜ்ஜியத்திற்கு அதிகமான சரியான பட்ஜெட்டை உள்ளிடவும்."
        )
    )

    fun get(key: String, lang: String): String {
        return translations[key]?.get(lang) ?: translations[key]?.get("en") ?: key
    }
}
