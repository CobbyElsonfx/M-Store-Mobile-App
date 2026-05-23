package com.momokotle.agent.data.form

import com.momokotle.agent.models.AgentFormType

data class FormTextField(
    val key: String,
    val label: String,
    val required: Boolean = true
)

data class FormUploadSlot(
    val key: String,
    val label: String,
    val instruction: String? = null,
    val required: Boolean = true
)

data class FormSection(
    val title: String,
    val fields: List<FormTextField> = emptyList(),
    val uploads: List<FormUploadSlot> = emptyList(),
    val instruction: String? = null
)

data class AgentFormDefinition(
    val formType: AgentFormType,
    val title: String,
    val sections: List<FormSection> = emptyList(),
    val standaloneFields: List<FormTextField> = emptyList(),
    val standaloneUploads: List<FormUploadSlot> = emptyList()
)

object AgentFormDefinitions {
    fun get(type: AgentFormType): AgentFormDefinition = when (type) {
        AgentFormType.AGENT_CARD_REQUEST -> agentCardRequest()
        AgentFormType.EVD_SIM_CONVERSION -> evdConversion()
        AgentFormType.DYNAMIC_SIM_BULK -> dynamicSimBulk()
        AgentFormType.TELECEL_AGENT -> telecelAgent()
        AgentFormType.AIRTELTIGO_AGENT -> airteltigoAgent()
    }

    val allForms: List<AgentFormDefinition> = AgentFormType.entries.map { get(it) }

    private fun agentCardRequest() = AgentFormDefinition(
        formType = AgentFormType.AGENT_CARD_REQUEST,
        title = "Agent Card Request Form",
        sections = listOf(
            FormSection(
                title = "Personal & Business Information",
                fields = listOf(
                    FormTextField("businessName", "Business Name"),
                    FormTextField("surname", "Surname"),
                    FormTextField("firstName", "First Name"),
                    FormTextField("phoneNumber", "Phone Number"),
                    FormTextField("dateOfBirth", "Date of Birth"),
                    FormTextField("placeOfBirth", "Place of Birth"),
                    FormTextField("ghanaCardNumber", "Ghana Card Number")
                )
            ),
            FormSection(
                title = "Next of Kin Details",
                fields = listOf(
                    FormTextField("nextOfKinName", "Next of Kin Name"),
                    FormTextField("nextOfKinPhone", "Next of Kin Phone Number")
                )
            ),
            FormSection(
                title = "Signature",
                instruction = "Sign on a plain sheet with blue pen, then upload",
                uploads = listOf(FormUploadSlot("signature", "Signature"))
            ),
            FormSection(
                title = "Required Document Uploads",
                uploads = listOf(
                    FormUploadSlot("ghanaCard", "Ghana Card"),
                    FormUploadSlot("businessCertificate", "Business Certificate"),
                    FormUploadSlot("formA", "Form A"),
                    FormUploadSlot("formB", "Form B"),
                    FormUploadSlot("formC", "Form C")
                )
            )
        )
    )

    private fun evdConversion() = AgentFormDefinition(
        formType = AgentFormType.EVD_SIM_CONVERSION,
        title = "EVD Agent SIM Conversion Form",
        standaloneFields = listOf(
            FormTextField("phoneNumber", "Phone Number"),
            FormTextField("ghanaCardId", "Ghana Card ID"),
            FormTextField("location", "Location")
        ),
        standaloneUploads = listOf(
            FormUploadSlot("signature", "Signature"),
            FormUploadSlot("ghanaCardFront", "Ghana Card Picture Front Only")
        )
    )

    private fun dynamicSimBulk() = AgentFormDefinition(
        formType = AgentFormType.DYNAMIC_SIM_BULK,
        title = "Dynamic SIM Cards Bulk Purchase Form",
        standaloneFields = listOf(
            FormTextField("phoneNumber", "Phone Number"),
            FormTextField("location", "Location"),
            FormTextField("whatsappNumber", "WhatsApp Number")
        ),
        standaloneUploads = listOf(FormUploadSlot("signature", "Signature"))
    )

    private fun telecelAgent() = AgentFormDefinition(
        formType = AgentFormType.TELECEL_AGENT,
        title = "Telecel Agent Details Form",
        sections = listOf(
            FormSection(
                title = "Required Documents",
                uploads = listOf(
                    FormUploadSlot("businessCertificateFormA", "Business Certificate including Form A"),
                    FormUploadSlot("ghanaCardFront", "Ghana Card Front Picture"),
                    FormUploadSlot("ghanaCardBack", "Ghana Card Back Picture"),
                    FormUploadSlot("holdingGhanaCardFront", "Applicant Holding Ghana Card Front Picture"),
                    FormUploadSlot("holdingGhanaCardBack", "Applicant Holding Ghana Card Back Picture")
                )
            )
        ),
        standaloneFields = listOf(
            FormTextField("phoneNumber", "Phone Number"),
            FormTextField("location", "Location")
        ),
        standaloneUploads = listOf(FormUploadSlot("signature", "Signature"))
    )

    private fun airteltigoAgent() = AgentFormDefinition(
        formType = AgentFormType.AIRTELTIGO_AGENT,
        title = "AirtelTigo Agent Details Form",
        sections = listOf(
            FormSection(
                title = "Required Documents",
                uploads = listOf(
                    FormUploadSlot("businessCertificateFormA", "Business Certificate including Form A"),
                    FormUploadSlot("ghanaCardFront", "Ghana Card Front Picture"),
                    FormUploadSlot("ghanaCardBack", "Ghana Card Back Picture")
                )
            )
        ),
        standaloneFields = listOf(
            FormTextField("phoneNumber", "Phone Number"),
            FormTextField("location", "Location")
        ),
        standaloneUploads = listOf(FormUploadSlot("signature", "Signature"))
    )
}
