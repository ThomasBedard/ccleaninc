import { useState } from "react";
import { useLanguage } from "../hooks/useLanguage"; // ✅ Import translations
import axios from "axios";
import "./Contacts.css";

const Contacts = () => {
  const { translations } = useLanguage(); // ✅ Get translations from context

  // ✅ Form state
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    subject: "",
    message: "",
  });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  // ✅ Handle input change
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // ✅ Handle form submission
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsSubmitting(true);
    setSuccessMessage("");
    setErrorMessage("");
  
    if (!formData.name || !formData.email || !formData.subject || !formData.message) {
      setErrorMessage(translations.contacts?.form?.error || "Please fill in all fields.");
      setIsSubmitting(false);
      return;
    }
  
    try {
      await axios.post("http://localhost:8080/api/v1/contact/send", {
        name: formData.name,       // ✅ Sender's name
        email: formData.email,     // ✅ Sender's email
        subject: formData.subject, // ✅ Email subject
        message: formData.message, // ✅ Message body
      });
  
      setSuccessMessage(translations.contacts?.form?.success || "Email sent successfully!");
      setFormData({ name: "", email: "", subject: "", message: "" });
    } catch (error) {
      console.error("Error sending email:", error); // ✅ Log the error
      setErrorMessage(
        translations.contacts?.form?.error || "Failed to send the email. Please try again."
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="contacts-container">
      <div className="contacts-header">
        <h1>{translations.contacts?.title || "Contact Us"}</h1>
        <p className="subtitle">{translations.contacts?.subtitle || "Feel free to reach out!"}</p>
      </div>

      <div className="contacts-content">
        <div className="contacts-details">
          <h2>{translations.contacts?.companyName || "C CLEAN INC."}</h2>
          <p>{translations.contacts?.address || "3042 RUE MASSON, QUEBEC"}</p>
          <p>{translations.contacts?.phone || "TEL: 514-555-5555"}</p>
          <p>{translations.contacts?.email || "EMAIL: fffffff@gmail.com"}</p>
        </div>

        <div className="contacts-form">
          <h2>{translations.contacts?.form?.title || "Send Us a Message"}</h2>
          {successMessage && <p className="success-message">{successMessage}</p>}
          {errorMessage && <p className="error-message">{errorMessage}</p>}

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>{translations.contacts?.form?.name || "Name"}</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                placeholder={translations.contacts?.form?.namePlaceholder || "Your Name"}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label>{translations.contacts?.form?.email || "Email"}</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                placeholder={translations.contacts?.form?.emailPlaceholder || "Your Email"}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label>{translations.contacts?.form?.subject || "Subject"}</label>
              <input
                type="text"
                name="subject"
                value={formData.subject}
                placeholder={translations.contacts?.form?.subjectPlaceholder || "Message Subject"}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label>{translations.contacts?.form?.message || "Message"}</label>
              <textarea
                name="message"
                value={formData.message}
                placeholder={translations.contacts?.form?.messagePlaceholder || "Write your message here..."}
                onChange={handleChange}
                required
              />
            </div>

            <button type="submit" disabled={isSubmitting}>
              {isSubmitting
                ? translations.contacts?.form?.sending || "Sending..."
                : translations.contacts?.form?.submit || "Send Message"}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Contacts;
