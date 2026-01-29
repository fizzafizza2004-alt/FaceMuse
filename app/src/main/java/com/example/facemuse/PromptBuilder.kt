object PromptBuilder {

    fun build(faceAnalysis: String): String {
        return """
        You are a professional skincare and makeup expert.

        User face analysis:
        $faceAnalysis

        Give:
        1. Skincare routine
        2. Makeup recommendations
        3. Daily care tips
        """.trimIndent()
    }
}
