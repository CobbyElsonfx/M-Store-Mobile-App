package com.momokotle.agent.data.form

import com.momokotle.agent.models.AgentFormType

data class FormTextField(
    val key: String,
    val label: String,
    val required: Boolean = true,
    val kind: FormFieldKind = FormFieldKind.TEXT,
    val placeholder: String? = null,
    val maxLength: Int? = null
)

data class FormUploadSlot(
    val key: String,
    val label: String,
    val instruction: String? = null,
    val required: Boolean = true,
    val kind: FormUploadKind = FormUploadKind.DOCUMENT
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
) {
    fun allFields(): List<FormTextField> =
        standaloneFields + sections.flatMap { it.fields }

    fun allUploads(): List<FormUploadSlot> =
        standaloneUploads + sections.flatMap { it.uploads }
}

object AgentFormDefinitions {
    fun get(type: AgentFormType): AgentFormDefinition = when (type) {
        AgentFormType.AGENT_CARD_REQUEST   -> agentCardRequest()
        AgentFormType.EVD_SIM_CONVERSION   -> evdConversion()
        AgentFormType.DYNAMIC_SIM_BULK     -> dynamicSimBulk()
        AgentFormType.TELECEL_AGENT        -> telecelAgent()
        AgentFormType.AIRTELTIGO_AGENT     -> airteltigoAgent()
        AgentFormType.BUSINESS_CERTIFICATE -> businessCertificate()
        AgentFormType.BIRTH_CERTIFICATE    -> birthCertificate()
    }

    val allForms: List<AgentFormDefinition> = AgentFormType.entries.map { get(it) }

    private fun agentCardRequest() = AgentFormDefinition(
        formType = AgentFormType.AGENT_CARD_REQUEST,
        title = "Agent Card Request Form",
        sections = listOf(
            FormSection(
                title = "Personal & Business Information",
                fields = listOf(
                    FormTextField("businessName", "Business Name", kind = FormFieldKind.NAME),
                    FormTextField("surname", "Surname", kind = FormFieldKind.NAME),
                    FormTextField("firstName", "First Name", kind = FormFieldKind.NAME),
                    FormTextField("phoneNumber", "Phone Number", kind = FormFieldKind.PHONE),
                    FormTextField("dateOfBirth", "Date of Birth", kind = FormFieldKind.DATE_OF_BIRTH),
                    FormTextField("placeOfBirth", "Place of Birth", kind = FormFieldKind.PLACE_OF_BIRTH),
                    FormTextField("ghanaCardNumber", "Ghana Card Number", kind = FormFieldKind.GHANA_CARD),
                    FormTextField("location", "Location / Address", kind = FormFieldKind.LOCATION)
                )
            ),
            FormSection(
                title = "Next of Kin Details",
                fields = listOf(
                    FormTextField("nextOfKinName", "Next of Kin Name", kind = FormFieldKind.NAME),
                    FormTextField("nextOfKinPhone", "Next of Kin Phone Number", kind = FormFieldKind.PHONE)
                )
            ),
            FormSection(
                title = "Signature",
                instruction = "Sign on a plain sheet with blue pen, then upload",
                uploads = listOf(
                    FormUploadSlot("signature", "Signature", kind = FormUploadKind.SIGNATURE)
                )
            ),
            FormSection(
                title = "Required Document Uploads",
                uploads = listOf(
                    FormUploadSlot("ghanaCardFront", "Ghana Card"),
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
        title = "EVD Agent and Sim Conversion Form",
        standaloneFields = listOf(
            FormTextField("phoneNumber", "Phone Number", kind = FormFieldKind.PHONE),
            FormTextField("ghanaCardNumber", "Ghana Card Number", kind = FormFieldKind.GHANA_CARD),
            FormTextField("location", "Location", kind = FormFieldKind.LOCATION)
        ),
        standaloneUploads = listOf(
            FormUploadSlot("signature", "Signature", kind = FormUploadKind.SIGNATURE),
            FormUploadSlot("ghanaCardFront", "Ghana Card Front")
        )
    )

    private fun dynamicSimBulk() = AgentFormDefinition(
        formType = AgentFormType.DYNAMIC_SIM_BULK,
        title = "Dynamic SIM Cards Bulk Purchase Form",
        standaloneFields = listOf(
            FormTextField("phoneNumber", "Phone Number", kind = FormFieldKind.PHONE),
            FormTextField("location", "Location", kind = FormFieldKind.LOCATION),
            FormTextField("whatsappNumber", "WhatsApp Number", kind = FormFieldKind.PHONE)
        ),
        standaloneUploads = listOf(
            FormUploadSlot("signature", "Signature", kind = FormUploadKind.SIGNATURE)
        )
    )

    private fun telecelAgent() = AgentFormDefinition(
        formType = AgentFormType.TELECEL_AGENT,
        title = "Telecel Agent Details Form",
        standaloneFields = listOf(
            FormTextField("businessName", "Business Name", kind = FormFieldKind.NAME),
            FormTextField("fullName", "Full Name", kind = FormFieldKind.NAME),
            FormTextField("phoneNumber", "Phone Number", kind = FormFieldKind.PHONE),
            FormTextField("dateOfBirth", "Date of Birth", kind = FormFieldKind.DATE_OF_BIRTH),
            FormTextField("location", "Location / Address", kind = FormFieldKind.LOCATION),
            FormTextField("ghanaCardNumber", "Ghana Card Number", kind = FormFieldKind.GHANA_CARD)
        ),
        sections = listOf(
            FormSection(
                title = "Required Documents",
                uploads = listOf(
                    FormUploadSlot("businessCertificate", "Business Certificate"),
                    FormUploadSlot("formA", "Form A"),
                    FormUploadSlot("ghanaCardFront", "Ghana Card Front"),
                    FormUploadSlot("ghanaCardBack", "Ghana Card Back"),
                    FormUploadSlot("applicantHoldingFront", "Applicant Holding Front"),
                    FormUploadSlot("applicantHoldingBack", "Applicant Holding Back")
                )
            )
        )
    )

    private fun airteltigoAgent() = AgentFormDefinition(
        formType = AgentFormType.AIRTELTIGO_AGENT,
        title = "AirtelTigo Agent Details Form",
        standaloneFields = listOf(
            FormTextField("businessName", "Business Name", kind = FormFieldKind.NAME),
            FormTextField("fullName", "Full Name", kind = FormFieldKind.NAME),
            FormTextField("phoneNumber", "Phone Number", kind = FormFieldKind.PHONE),
            FormTextField("dateOfBirth", "Date of Birth", kind = FormFieldKind.DATE_OF_BIRTH),
            FormTextField("location", "Location / Address", kind = FormFieldKind.LOCATION),
            FormTextField("ghanaCardNumber", "Ghana Card Number", kind = FormFieldKind.GHANA_CARD)
        ),
        sections = listOf(
            FormSection(
                title = "Required Documents",
                uploads = listOf(
                    FormUploadSlot("businessCertificate", "Business Certificate"),
                    FormUploadSlot("formA", "Form A"),
                    FormUploadSlot("ghanaCardFront", "Ghana Card Front"),
                    FormUploadSlot("ghanaCardBack", "Ghana Card Back")
                )
            )
        )
    )

    private fun businessCertificate() = AgentFormDefinition(
        formType = AgentFormType.BUSINESS_CERTIFICATE,
        title = "Business Certificate Application",
        sections = listOf(
            FormSection(
                title = "Business Details",
                fields = listOf(
                    FormTextField("businessName1", "Business Name Option 1", kind = FormFieldKind.NAME),
                    FormTextField("businessName2", "Business Name Option 2", kind = FormFieldKind.NAME),
                    FormTextField("businessName3", "Business Name Option 3", kind = FormFieldKind.NAME),
                    FormTextField("natureOfBusiness", "Nature of Business")
                )
            ),
            FormSection(
                title = "Business Address",
                fields = listOf(
                    FormTextField("bizHouseNumber", "House Number"),
                    FormTextField("bizStreet", "Street"),
                    FormTextField("bizArea", "Area"),
                    FormTextField("bizCity", "City"),
                    FormTextField("bizRegion", "Region"),
                    FormTextField("bizNearestLandmark", "Nearest Landmark"),
                    FormTextField("bizDigitalAddress", "Digital Address")
                )
            ),
            FormSection(
                title = "Personal Information",
                fields = listOf(
                    FormTextField("firstName", "First Name", kind = FormFieldKind.NAME),
                    FormTextField("surname", "Surname", kind = FormFieldKind.NAME),
                    FormTextField("dateOfBirth", "Date of Birth", kind = FormFieldKind.DATE_OF_BIRTH),
                    FormTextField("occupation", "Occupation"),
                    FormTextField("nationality", "Nationality"),
                    FormTextField("gender", "Gender"),
                    FormTextField("maritalStatus", "Marital Status")
                )
            ),
            FormSection(
                title = "Residential Address",
                fields = listOf(
                    FormTextField("resHouseNumber", "House Number"),
                    FormTextField("resStreet", "Street"),
                    FormTextField("resArea", "Area"),
                    FormTextField("resCity", "City"),
                    FormTextField("resDistrict", "District"),
                    FormTextField("resRegion", "Region"),
                    FormTextField("resNearestLandmark", "Nearest Landmark"),
                    FormTextField("resDigitalAddress", "Digital Address Code")
                )
            ),
            FormSection(
                title = "Postal Address",
                fields = listOf(
                    FormTextField("poBox", "P.O. Box"),
                    FormTextField("postalCity", "City"),
                    FormTextField("postalRegion", "Region")
                )
            ),
            FormSection(
                title = "Contact & Identity",
                fields = listOf(
                    FormTextField("phoneNumber", "Phone Number", kind = FormFieldKind.PHONE),
                    FormTextField("emailAddress", "Email Address"),
                    FormTextField("tinNumber", "TIN Number"),
                    FormTextField("ghanaCardNumber", "Ghana Card Number", kind = FormFieldKind.GHANA_CARD)
                )
            ),
            FormSection(
                title = "Document Uploads",
                instruction = "Sign on a plain A4 or white sheet, then photograph or scan it",
                uploads = listOf(
                    FormUploadSlot("signature", "Signature", kind = FormUploadKind.SIGNATURE),
                    FormUploadSlot("ghanaCardFront", "Ghana Card Front Image"),
                    FormUploadSlot("ghanaCardBack", "Ghana Card Back Image")
                )
            )
        )
    )

    private fun birthCertificate() = AgentFormDefinition(
        formType = AgentFormType.BIRTH_CERTIFICATE,
        title = "Birth Certificate Application",
        standaloneFields = listOf(
            FormTextField("fullName", "Full Name", kind = FormFieldKind.NAME),
            FormTextField("dateOfBirth", "Date of Birth", kind = FormFieldKind.DATE_OF_BIRTH),
            FormTextField("placeOfBirth", "Place of Birth", kind = FormFieldKind.PLACE_OF_BIRTH),
            FormTextField("gender", "Gender"),
            FormTextField("motherName", "Mother's Full Name", kind = FormFieldKind.NAME),
            FormTextField("fatherName", "Father's Full Name", kind = FormFieldKind.NAME),
            FormTextField("locationOfBirth", "Location of Birth", kind = FormFieldKind.LOCATION),
            FormTextField("phoneNumber", "Phone Number", kind = FormFieldKind.PHONE),
            FormTextField(
                "ghanaCardNumber",
                "Ghana Card Number (if available)",
                required = false,
                kind = FormFieldKind.GHANA_CARD
            )
        ),
        standaloneUploads = listOf(
            FormUploadSlot(
                key = "birthProof",
                label = "Birth Proof Document",
                instruction = "Upload any supporting birth document if available",
                required = false
            ),
            FormUploadSlot("ghanaCard", "Ghana Card (if available)", required = false),
            FormUploadSlot("signature", "Signature", kind = FormUploadKind.SIGNATURE)
        )
    )
}
