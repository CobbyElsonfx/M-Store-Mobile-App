package com.momokotle.agent.data.form

import com.momokotle.agent.models.AgentFormType

data class FormTextField(
    val key: String,
    val label: String,
    val required: Boolean = true,
    val kind: FormFieldKind = FormFieldKind.TEXT,
    val placeholder: String? = null,
    val maxLength: Int? = null,
    val options: List<String> = emptyList(),
    val visibleWhenKey: String? = null,
    val visibleWhenValue: String? = null,
    val requiredWhenKey: String? = null,
    val requiredWhenValue: String? = null
) {
    fun isVisible(values: Map<String, String>): Boolean {
        val key = visibleWhenKey ?: return true
        return values[key].orEmpty() == (visibleWhenValue ?: "")
    }

    fun isEffectivelyRequired(values: Map<String, String>): Boolean {
        if (!isVisible(values)) return false
        val requirementKey = requiredWhenKey ?: return required
        return values[requirementKey].orEmpty() == (requiredWhenValue ?: "")
    }
}

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

data class FormStep(
    val title: String,
    val subtitle: String? = null,
    val sectionTitles: List<String> = emptyList(),
    val includeStandalone: Boolean = false,
    val isReview: Boolean = false
)

data class AgentFormDefinition(
    val formType: AgentFormType,
    val title: String,
    val sections: List<FormSection> = emptyList(),
    val standaloneFields: List<FormTextField> = emptyList(),
    val standaloneUploads: List<FormUploadSlot> = emptyList(),
    val steps: List<FormStep> = emptyList()
) {
    fun allFields(): List<FormTextField> =
        standaloneFields + sections.flatMap { it.fields }

    fun allUploads(): List<FormUploadSlot> =
        standaloneUploads + sections.flatMap { it.uploads }

    fun isStepped(): Boolean = steps.isNotEmpty()

    fun sectionsForStep(index: Int): List<FormSection> {
        val step = steps.getOrNull(index) ?: return sections
        if (step.isReview) return emptyList()
        if (step.sectionTitles.isEmpty()) return sections
        return sections.filter { it.title in step.sectionTitles }
    }

    fun includeStandaloneForStep(index: Int): Boolean {
        if (steps.isEmpty()) return true
        return steps.getOrNull(index)?.includeStandalone == true
    }
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
        AgentFormType.PASSPORT             -> passportApplication()
    }

    val allForms: List<AgentFormDefinition> = AgentFormType.entries.map { get(it) }

    private fun agentCardRequest() = AgentFormDefinition(
        formType = AgentFormType.AGENT_CARD_REQUEST,
        title = "Agent Card Request Form",
        sections = listOf(
            FormSection(
                title = "Personal & Business Information",
                fields = listOf(
                    FormTextField(
                        "businessName",
                        "Business Name",
                        kind = FormFieldKind.BUSINESS_NAME,
                        placeholder = "ADDO 4 Ent."
                    ),
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
            FormTextField(
                "businessName",
                "Business Name",
                kind = FormFieldKind.BUSINESS_NAME,
                placeholder = "SMART-LINK ENTERPRISE"
            ),
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
            FormTextField(
                "businessName",
                "Business Name",
                kind = FormFieldKind.BUSINESS_NAME,
                placeholder = "SMART-LINK ENTERPRISE"
            ),
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
                    FormTextField(
                        "businessName1",
                        "Business Name Option 1",
                        kind = FormFieldKind.BUSINESS_NAME,
                        placeholder = "ADDO 4 Ent."
                    ),
                    FormTextField(
                        "businessName2",
                        "Business Name Option 2",
                        kind = FormFieldKind.BUSINESS_NAME,
                        required = false,
                        placeholder = "KOFI 24 VENTURES"
                    ),
                    FormTextField(
                        "businessName3",
                        "Business Name Option 3",
                        kind = FormFieldKind.BUSINESS_NAME,
                        required = false,
                        placeholder = "A.B.C ENTERPRISE"
                    ),
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
                    FormTextField(
                        "bizRegion",
                        "Region",
                        kind = FormFieldKind.SELECT,
                        placeholder = "Select region",
                        options = FormOptions.GHANA_REGIONS
                    ),
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
                    FormTextField(
                        "gender",
                        "Gender",
                        kind = FormFieldKind.SELECT,
                        placeholder = "Select gender",
                        options = FormOptions.GENDER_OPTIONS
                    ),
                    FormTextField(
                        "maritalStatus",
                        "Marital Status",
                        kind = FormFieldKind.SELECT,
                        placeholder = "Select status",
                        options = FormOptions.MARITAL_STATUS_OPTIONS
                    )
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
                    FormTextField(
                        "resRegion",
                        "Region",
                        kind = FormFieldKind.SELECT,
                        placeholder = "Select region",
                        options = FormOptions.GHANA_REGIONS
                    ),
                    FormTextField("resNearestLandmark", "Nearest Landmark"),
                    FormTextField("resDigitalAddress", "Digital Address Code")
                )
            ),
            FormSection(
                title = "Postal Address",
                fields = listOf(
                    FormTextField("poBox", "P.O. Box"),
                    FormTextField("postalCity", "City"),
                    FormTextField(
                        "postalRegion",
                        "Region",
                        kind = FormFieldKind.SELECT,
                        placeholder = "Select region",
                        options = FormOptions.GHANA_REGIONS
                    )
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
            FormTextField(
                "gender",
                "Gender",
                kind = FormFieldKind.SELECT,
                placeholder = "Select gender",
                options = FormOptions.GENDER_OPTIONS
            ),
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

    private fun passportApplication() = AgentFormDefinition(
            formType = AgentFormType.PASSPORT,
            title = "Passport Application",
            steps = listOf(
                FormStep("Applicant Information", "Step 1 of 7", listOf(
                    "Applicant Information",
                    "Document Information",
                    "Marital Status & Profession",
                    "Contact & Address"
                )),
                FormStep("Education", "Step 2 of 7", listOf("Applicant Education")),
                FormStep("Parents Details", "Step 3 of 7", listOf("Father's Details", "Mother's Details")),
                FormStep("Grandparent Details", "Step 4 of 7", listOf("Applicant Grandparent")),
                FormStep("Guarantors", "Step 5 of 7", listOf("Guarantor 1", "Guarantor 2")),
                FormStep("Witness", "Step 6 of 7", listOf("Applicant Witness", "Qualified Witness", "Signature")),
                FormStep("Review & Submit", "Step 7 of 7", isReview = true)
            ),
            sections = listOf(
                FormSection(
                    title = "Applicant Information",
                    fields = listOf(
                        FormTextField("surname", "Surname", kind = FormFieldKind.NAME),
                        FormTextField("firstName", "First Name", kind = FormFieldKind.NAME),
                        FormTextField("otherName", "Other Name", required = false, kind = FormFieldKind.NAME),
                        FormTextField(
                            "gender",
                            "Gender",
                            kind = FormFieldKind.SELECT,
                            placeholder = "Select gender",
                            options = FormOptions.GENDER_OPTIONS
                        ),
                        FormTextField("dateOfBirth", "Date of Birth", kind = FormFieldKind.DATE),
                        FormTextField("cityOfBirth", "City/Town of Birth", kind = FormFieldKind.PLACE_OF_BIRTH),
                        FormTextField("countryOfBirth", "Country of Birth", kind = FormFieldKind.LOCATION),
                        FormTextField("height", "Height", placeholder = "e.g. 1.70 m"),
                        FormTextField("nationality", "Nationality", kind = FormFieldKind.LOCATION, placeholder = "Ghanaian")
                    )
                ),
                FormSection(
                    title = "Document Information",
                    fields = listOf(
                        FormTextField("oldPassportNumber", "Old Passport Number"),
                        FormTextField("oldPassportDateOfIssue", "Old Passport Date of Issue", kind = FormFieldKind.DATE),
                        FormTextField("socialSecurityNumber", "Social Security Number"),
                        FormTextField("voterIdNumber", "Voter ID Number"),
                        FormTextField("birthCertificateNumber", "Birth Certificate Number"),
                        FormTextField(
                            "birthCertificateDateOfIssue",
                            "Birth Certificate Date of Issue",
                            kind = FormFieldKind.DATE
                        ),
                        FormTextField(
                            "birthCertificatePlaceOfIssue",
                            "Birth Certificate Place of Issue",
                            kind = FormFieldKind.LOCATION
                        )
                    )
                ),
                FormSection(
                    title = "Marital Status & Profession",
                    fields = listOf(
                        FormTextField(
                            "maritalStatus",
                            "Marital Status",
                            kind = FormFieldKind.SELECT,
                            placeholder = "Select marital status",
                            options = FormOptions.PASSPORT_MARITAL_STATUS_OPTIONS
                        ),
                        FormTextField("newPassportProfession", "New Passport Profession"),
                        FormTextField("oldPassportProfession", "Old Passport Profession")
                    )
                ),
                FormSection(
                    title = "Contact & Address",
                    fields = listOf(
                        FormTextField("phoneNumber", "Telephone Number", kind = FormFieldKind.PHONE),
                        FormTextField("emailAddress", "Email Address", kind = FormFieldKind.EMAIL),
                        FormTextField("houseNumber", "House Number"),
                        FormTextField("cityOfResidence", "City of Residence", kind = FormFieldKind.LOCATION),
                        FormTextField("houseStreetName", "House Street Name", kind = FormFieldKind.LOCATION),
                        FormTextField("postOfficeBox", "Post Office Box", required = false)
                    )
                ),
                FormSection(
                    title = "Applicant Education",
                    fields = listOf(
                        FormTextField(
                            "hasFormalEducation",
                            "Has the applicant received any form of formal education?",
                            kind = FormFieldKind.SELECT,
                            placeholder = "Select Yes or No",
                            options = FormOptions.YES_NO_OPTIONS
                        ),
                        FormTextField(
                            "schoolName",
                            "Name of School",
                            visibleWhenKey = "hasFormalEducation",
                            visibleWhenValue = "Yes"
                        ),
                        FormTextField(
                            "schoolMonthStarted",
                            "Month Started",
                            kind = FormFieldKind.SELECT,
                            options = FormOptions.MONTH_OPTIONS,
                            visibleWhenKey = "hasFormalEducation",
                            visibleWhenValue = "Yes"
                        ),
                        FormTextField(
                            "schoolYearStarted",
                            "Year Started",
                            kind = FormFieldKind.YEAR,
                            visibleWhenKey = "hasFormalEducation",
                            visibleWhenValue = "Yes"
                        ),
                        FormTextField(
                            "schoolMonthEnded",
                            "Month Ended",
                            kind = FormFieldKind.SELECT,
                            options = FormOptions.MONTH_OPTIONS,
                            visibleWhenKey = "hasFormalEducation",
                            visibleWhenValue = "Yes"
                        ),
                        FormTextField(
                            "schoolYearEnded",
                            "Year Ended",
                            kind = FormFieldKind.YEAR,
                            visibleWhenKey = "hasFormalEducation",
                            visibleWhenValue = "Yes"
                        ),
                        FormTextField(
                            "schoolAddress",
                            "School Address",
                            kind = FormFieldKind.LOCATION,
                            visibleWhenKey = "hasFormalEducation",
                            visibleWhenValue = "Yes"
                        )
                    )
                ),
                FormSection(
                    title = "Father's Details",
                    fields = parentFields("father")
                ),
                FormSection(
                    title = "Mother's Details",
                    fields = parentFields("mother")
                ),
                FormSection(
                    title = "Applicant Grandparent",
                    fields = listOf(
                        FormTextField("grandparentFullName", "Full Name", kind = FormFieldKind.NAME),
                        FormTextField(
                            "grandparentLivingStatus",
                            "Living / Dead",
                            kind = FormFieldKind.SELECT,
                            options = FormOptions.LIVING_STATUS_OPTIONS
                        ),
                        FormTextField(
                            "grandparentTelephone",
                            "Telephone",
                            kind = FormFieldKind.PHONE,
                            required = false,
                            requiredWhenKey = "grandparentLivingStatus",
                            requiredWhenValue = "Living"
                        ),
                        FormTextField("grandparentHometown", "Home Town", kind = FormFieldKind.LOCATION),
                        FormTextField(
                            "grandparentHouseNumber",
                            "House Number",
                            required = false,
                            requiredWhenKey = "grandparentLivingStatus",
                            requiredWhenValue = "Living"
                        ),
                        FormTextField("grandparentPostOfficeBox", "Post Office Box Number", required = false),
                        FormTextField("grandparentNationality", "Nationality", kind = FormFieldKind.LOCATION)
                    )
                ),
                FormSection(
                    title = "Guarantor 1",
                    fields = guarantorFields("guarantor1")
                ),
                FormSection(
                    title = "Guarantor 2",
                    fields = guarantorFields("guarantor2")
                ),
                FormSection(
                    title = "Applicant Witness",
                    fields = listOf(
                        FormTextField("witnessFullName", "Full Name", kind = FormFieldKind.NAME),
                        FormTextField("witnessOccupation", "Occupation"),
                        FormTextField("witnessWorkAddress", "Work Address", kind = FormFieldKind.LOCATION),
                        FormTextField("witnessWorkTelephone", "Work Telephone Number", kind = FormFieldKind.PHONE),
                        FormTextField("witnessHomeTelephone", "Home Telephone Number", kind = FormFieldKind.PHONE),
                        FormTextField("witnessHouseAddress", "House Address", kind = FormFieldKind.LOCATION)
                    )
                ),
                FormSection(
                    title = "Qualified Witness",
                    fields = listOf(
                        FormTextField(
                            "qualifiedWitness",
                            "Qualified Witness",
                            kind = FormFieldKind.SELECT,
                            placeholder = "Select qualification",
                            options = FormOptions.WITNESS_QUALIFICATION_OPTIONS
                        )
                    )
                ),
                FormSection(
                    title = "Signature",
                    instruction = "Sign on a plain sheet with a blue pen, then upload",
                    uploads = listOf(
                        FormUploadSlot("signature", "Signature", kind = FormUploadKind.SIGNATURE)
                    )
                )
            )
        )

    private fun parentFields(prefix: String): List<FormTextField> {
        val statusKey = "${prefix}LivingStatus"
        val labelPrefix = if (prefix == "father") "Father" else "Mother"
        return listOf(
            FormTextField("${prefix}FullName", "$labelPrefix Full Name", kind = FormFieldKind.NAME),
            FormTextField(
                statusKey,
                "Living / Dead",
                kind = FormFieldKind.SELECT,
                options = FormOptions.LIVING_STATUS_OPTIONS
            ),
            FormTextField(
                "${prefix}Telephone",
                "Telephone",
                kind = FormFieldKind.PHONE,
                required = false,
                requiredWhenKey = statusKey,
                requiredWhenValue = "Living"
            ),
            FormTextField(
                "${prefix}Email",
                "Email",
                kind = FormFieldKind.EMAIL,
                required = false,
                requiredWhenKey = statusKey,
                requiredWhenValue = "Living"
            ),
            FormTextField("${prefix}Hometown", "Hometown", kind = FormFieldKind.LOCATION),
            FormTextField(
                "${prefix}HouseNumber",
                "House Number",
                required = false,
                requiredWhenKey = statusKey,
                requiredWhenValue = "Living"
            ),
            FormTextField("${prefix}PostOfficeBox", "Post Office Box Number", required = false),
            FormTextField("${prefix}Nationality", "Nationality", kind = FormFieldKind.LOCATION)
        )
    }

    private fun guarantorFields(prefix: String): List<FormTextField> = listOf(
        FormTextField("${prefix}FullName", "Full Name", kind = FormFieldKind.NAME),
        FormTextField("${prefix}Occupation", "Occupation"),
        FormTextField("${prefix}Telephone", "Telephone", kind = FormFieldKind.PHONE),
        FormTextField("${prefix}Email", "Email", kind = FormFieldKind.EMAIL),
        FormTextField("${prefix}HouseNumber", "House Number"),
        FormTextField("${prefix}PostOfficeBox", "Post Office Box Number", required = false)
    )
}
