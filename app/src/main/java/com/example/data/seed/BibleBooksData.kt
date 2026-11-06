package com.example.data.seed

import com.example.data.model.BibleBook
import com.example.data.model.BibleCategoryGroup
import com.example.data.model.Testament

object BibleBooksData {
    val allBooks: List<BibleBook> = listOf(
        // === OLD TESTAMENT (पुरानो नियम - ३९ पुस्तकहरू) ===
        // व्यवस्था (Law / Pentateuch)
        BibleBook(1, "उत्पत्ति", "Genesis", Testament.OLD, BibleCategoryGroup.LAW, 50, "उत्प"),
        BibleBook(2, "प्रस्थान", "Exodus", Testament.OLD, BibleCategoryGroup.LAW, 40, "प्रस्थ"),
        BibleBook(3, "लेवी", "Leviticus", Testament.OLD, BibleCategoryGroup.LAW, 27, "लेवी"),
        BibleBook(4, "गन्ती", "Numbers", Testament.OLD, BibleCategoryGroup.LAW, 36, "गन्ती"),
        BibleBook(5, "व्यवस्था", "Deuteronomy", Testament.OLD, BibleCategoryGroup.LAW, 34, "व्यव"),

        // इतिहास (History)
        BibleBook(6, "यहोशू", "Joshua", Testament.OLD, BibleCategoryGroup.HISTORY, 24, "यहो"),
        BibleBook(7, "न्यायकर्ता", "Judges", Testament.OLD, BibleCategoryGroup.HISTORY, 21, "न्याय"),
        BibleBook(8, "रूथ", "Ruth", Testament.OLD, BibleCategoryGroup.HISTORY, 4, "रूथ"),
        BibleBook(9, "१ शमूएल", "1 Samuel", Testament.OLD, BibleCategoryGroup.HISTORY, 31, "१ शमू"),
        BibleBook(10, "२ शमूएल", "2 Samuel", Testament.OLD, BibleCategoryGroup.HISTORY, 24, "२ शमू"),
        BibleBook(11, "१ राजा", "1 Kings", Testament.OLD, BibleCategoryGroup.HISTORY, 22, "१ राजा"),
        BibleBook(12, "२ राजा", "2 Kings", Testament.OLD, BibleCategoryGroup.HISTORY, 25, "२ राजा"),
        BibleBook(13, "१ इतिहास", "1 Chronicles", Testament.OLD, BibleCategoryGroup.HISTORY, 29, "१ इति"),
        BibleBook(14, "२ इतिहास", "2 Chronicles", Testament.OLD, BibleCategoryGroup.HISTORY, 36, "२ इति"),
        BibleBook(15, "एज्रा", "Ezra", Testament.OLD, BibleCategoryGroup.HISTORY, 10, "एज्रा"),
        BibleBook(16, "नहेम्याह", "Nehemiah", Testament.OLD, BibleCategoryGroup.HISTORY, 13, "नहे"),
        BibleBook(17, "एस्तर", "Esther", Testament.OLD, BibleCategoryGroup.HISTORY, 10, "एस्तर"),

        // कविता र बुद्धि (Poetry & Wisdom)
        BibleBook(18, "अय्यूब", "Job", Testament.OLD, BibleCategoryGroup.POETRY, 42, "अय्यू"),
        BibleBook(19, "भजनसंग्रह", "Psalms", Testament.OLD, BibleCategoryGroup.POETRY, 150, "भजन"),
        BibleBook(20, "हितोपदेश", "Proverbs", Testament.OLD, BibleCategoryGroup.POETRY, 31, "हितो"),
        BibleBook(21, "उपदेशक", "Ecclesiastes", Testament.OLD, BibleCategoryGroup.POETRY, 12, "उपदे"),
        BibleBook(22, "श्रेष्ठगीत", "Song of Solomon", Testament.OLD, BibleCategoryGroup.POETRY, 8, "श्रेष्ठ"),

        // अगमवाणी (Major & Minor Prophets)
        BibleBook(23, "यशैया", "Isaiah", Testament.OLD, BibleCategoryGroup.PROPHETS, 66, "यशै"),
        BibleBook(24, "यर्मिया", "Jeremiah", Testament.OLD, BibleCategoryGroup.PROPHETS, 52, "यर्मि"),
        BibleBook(25, "विलाप", "Lamentations", Testament.OLD, BibleCategoryGroup.PROPHETS, 5, "विला"),
        BibleBook(26, "इजकिएल", "Ezekiel", Testament.OLD, BibleCategoryGroup.PROPHETS, 48, "इजकि"),
        BibleBook(27, "दानिएल", "Daniel", Testament.OLD, BibleCategoryGroup.PROPHETS, 12, "दानि"),
        BibleBook(28, "होशे", "Hosea", Testament.OLD, BibleCategoryGroup.PROPHETS, 14, "होशे"),
        BibleBook(29, "योएल", "Joel", Testament.OLD, BibleCategoryGroup.PROPHETS, 3, "योएल"),
        BibleBook(30, "आमोस", "Amos", Testament.OLD, BibleCategoryGroup.PROPHETS, 9, "आमोस"),
        BibleBook(31, "ओबद्याह", "Obadiah", Testament.OLD, BibleCategoryGroup.PROPHETS, 1, "ओबद्या"),
        BibleBook(32, "योना", "Jonah", Testament.OLD, BibleCategoryGroup.PROPHETS, 4, "योना"),
        BibleBook(33, "मीका", "Micah", Testament.OLD, BibleCategoryGroup.PROPHETS, 7, "मीका"),
        BibleBook(34, "नहूम", "Nahum", Testament.OLD, BibleCategoryGroup.PROPHETS, 3, "नहूम"),
        BibleBook(35, "हबकूक", "Habakkuk", Testament.OLD, BibleCategoryGroup.PROPHETS, 3, "हबकूक"),
        BibleBook(36, "सपन्याह", "Zephaniah", Testament.OLD, BibleCategoryGroup.PROPHETS, 3, "सपन्या"),
        BibleBook(37, "हाग्गै", "Haggai", Testament.OLD, BibleCategoryGroup.PROPHETS, 2, "हाग्गै"),
        BibleBook(38, "जकरिया", "Zechariah", Testament.OLD, BibleCategoryGroup.PROPHETS, 14, "जकरि"),
        BibleBook(39, "मलाकी", "Malachi", Testament.OLD, BibleCategoryGroup.PROPHETS, 4, "मलाकी"),

        // === NEW TESTAMENT (नयाँ नियम - २७ पुस्तकहरू) ===
        // सुसमाचार (Gospels)
        BibleBook(40, "मत्ती", "Matthew", Testament.NEW, BibleCategoryGroup.GOSPEL, 28, "मत्ती"),
        BibleBook(41, "मर्कूस", "Mark", Testament.NEW, BibleCategoryGroup.GOSPEL, 16, "मर्कू"),
        BibleBook(42, "लूका", "Luke", Testament.NEW, BibleCategoryGroup.GOSPEL, 24, "लूका"),
        BibleBook(43, "यूहन्ना", "John", Testament.NEW, BibleCategoryGroup.GOSPEL, 21, "यूह"),

        // मण्डलीको इतिहास (Church History)
        BibleBook(44, "प्रेरित", "Acts", Testament.NEW, BibleCategoryGroup.CHURCH_HISTORY, 28, "प्रेरि"),

        // पावलका पत्रहरू (Pauline Epistles)
        BibleBook(45, "रोमी", "Romans", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 16, "रोमी"),
        BibleBook(46, "१ कोरिन्थी", "1 Corinthians", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 16, "१ कोरि"),
        BibleBook(47, "२ कोरिन्थी", "2 Corinthians", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 13, "२ कोरि"),
        BibleBook(48, "गलाती", "Galatians", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 6, "गला"),
        BibleBook(49, "एफिसी", "Ephesians", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 6, "एफि"),
        BibleBook(50, "फिलिप्पी", "Philippians", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 4, "फिलि"),
        BibleBook(51, "कलस्सी", "Colossians", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 4, "कल"),
        BibleBook(52, "१ थिस्सलोनिकी", "1 Thessalonians", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 5, "१ थिस्स"),
        BibleBook(53, "२ थिस्सलोनिकी", "2 Thessalonians", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 3, "२ थिस्स"),
        BibleBook(54, "१ तिमोथी", "1 Timothy", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 6, "१ तिमो"),
        BibleBook(55, "२ तिमोथी", "2 Timothy", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 4, "२ तिमो"),
        BibleBook(56, "तीतस", "Titus", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 3, "तीतस"),
        BibleBook(57, "फिलेमोन", "Philemon", Testament.NEW, BibleCategoryGroup.PAULINE_EPISTLES, 1, "फिले"),

        // सामान्य पत्रहरू (General Epistles)
        BibleBook(58, "हिब्रू", "Hebrews", Testament.NEW, BibleCategoryGroup.GENERAL_EPISTLES, 13, "हिब्रू"),
        BibleBook(59, "याकूब", "James", Testament.NEW, BibleCategoryGroup.GENERAL_EPISTLES, 5, "याकूब"),
        BibleBook(60, "१ पत्रुस", "1 Peter", Testament.NEW, BibleCategoryGroup.GENERAL_EPISTLES, 5, "१ पत्रु"),
        BibleBook(61, "२ पत्रुस", "2 Peter", Testament.NEW, BibleCategoryGroup.GENERAL_EPISTLES, 3, "२ पत्रु"),
        BibleBook(62, "१ यूहन्ना", "1 John", Testament.NEW, BibleCategoryGroup.GENERAL_EPISTLES, 5, "१ यूह"),
        BibleBook(63, "२ यूहन्ना", "2 John", Testament.NEW, BibleCategoryGroup.GENERAL_EPISTLES, 1, "२ यूह"),
        BibleBook(64, "३ यूहन्ना", "3 John", Testament.NEW, BibleCategoryGroup.GENERAL_EPISTLES, 1, "३ यूह"),
        BibleBook(65, "यहूदा", "Jude", Testament.NEW, BibleCategoryGroup.GENERAL_EPISTLES, 1, "यहूदा"),

        // प्रकाश (Prophecy / Apocalypse)
        BibleBook(66, "प्रकाश", "Revelation", Testament.NEW, BibleCategoryGroup.REVELATION, 22, "प्रका")
    )

    fun getBookById(id: Int): BibleBook? = allBooks.find { it.id == id }
    fun getBookByName(name: String): BibleBook? = allBooks.find {
        it.nepaliName.equals(name, ignoreCase = true) || it.englishName.equals(name, ignoreCase = true)
    }
}
