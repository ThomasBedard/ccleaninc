import { createContext } from "react";

// Define language types
export type Language = "en" | "fr";

// Define translations interface
export interface Translations {
  navbar?: {
    brand?: string;
    home?: string;
    services?: string;
    appointments?: string;
    schedule?: string;
    employees?: string;
    admin_dashboard?: string;
    about_us?: string;
    contacts?: string;
    my_appointments?: string;
    my_availabilities?: string;
    language_switch?: string;
    profile?: string;
  };
  home?: {
    title?: string;
    description?: string;
    learnMore?: string;
    customerFeedbacks?: string;
    submitFeedback?: string;
    loadingFeedbacks?: string;
    noFeedback?: string;
    customer?: string;
  };
  services?: {
    title?: string;
    searchPlaceholder?: string;
    searchButton?: string;
    loading?: string;
    errorMessage?: string;
    noServicesFound?: string;
    pricing?: string;
    availability?: {
      available?: string;
      unavailable?: string;
    };
    select?: string;
    delete?: string;
    editService?: string;
    addService?: string;
    continueWithSelected?: string;
  };
  contacts?: {
    title?: string;
    subtitle?: string;
    companyName?: string;
    address?: string;
    phone?: string;
    email?: string;
    form?: {  // ✅ Fix: Added missing 'form' definition
      title?: string;
      name?: string;
      namePlaceholder?: string;
      email?: string;
      emailPlaceholder?: string;
      subject?: string;
      subjectPlaceholder?: string;
      message?: string;
      messagePlaceholder?: string;
      submit?: string;
      sending?: string;
      success?: string;
      error?: string;
    };
  };
  about_us?: {
    header?: string;
    subtitle?: string;
    body?: string;
  };
  appointments?: {
    title?: string;
    actions?: {
      add?: string;
      download_pdf?: string;
    };
    table?: {
      appointment_id?: string;
      customer_id?: string;
      first_name?: string;
      last_name?: string;
      date?: string;
      services?: string;
      status?: string;
      comments?: string;
      actions?: string;
      edit?: string;
      delete?: string;
    };
    messages?: {
      loading?: string;
      error?: string;
      delete_confirm?: string;
      delete_success?: string;
      delete_error?: string;
      pdf_download_error?: string;
    };
  };
  select_date_time?: {  
    title?: string;
    selected_date?: string;
    select_time?: string;
    error?: string;
    next_button?: string;
    invalid_date?: string;
    invalid_time?: string;
  };
  checkout?: {  
    title?: string;
    selected_services?: string;
    no_services?: string;
    date_time?: string;
    customer_id?: string;
    first_name?: string;
    last_name?: string;
    comments?: string;
    comments_placeholder?: string;
    error?: {
      invalid_fields?: string;
      invalid_customer?: string;
      no_date?: string;
      fetch_services?: string;
      fetch_customer?: string;
      appointment_creation?: string;
    };
    success?: {
      appointment_created?: string;
    };
    confirm_button?: string;
    confirming?: string;
    total?: string;
  };
  profile?: {
    title?: string;
    loading?: string;
    loading_profile?: string;
    not_logged_in?: string;
    prompt?: string;
    yes?: string;
    no?: string;
    opt_out?: string;
    create_profile_anyway?: string;
    edit_profile?: string;
    save_profile?: string;
    cancel?: string;
    customer_id?: string;
    first_name?: string;
    last_name?: string;
    phone?: string;
    company?: string;
    address?: string;
    success?: {
      update_success?: string;
    };
    error?: {
      first_name_required?: string;
      last_name_required?: string;
      address_required?: string;
      invalid_address?: string;
      invalid_phone?: string;
      update_failed?: string;
    };
  };
  myAppointments?: {
    title?: string;
    showing_for?: string;
    filter?: {
      label?: string;
      all?: string;
      pending?: string;
      confirmed?: string;
      cancelled?: string;
    };
    status?: {
      pending?: string;
      confirmed?: string;
      cancelled?: string;
    };
    comments?: string;
    no_appointments?: string;
    no_filtered_appointments?: string;
    error?: {
      no_email?: string;
      fetch_failed?: string;
    };
  };
  myAvailabilities?: {
    title?: string;
    showing_for?: string;
    table?: {
      availability_id?: string;
      employee_name?: string;
      date_time?: string;
      shift?: string;
      comments?: string;
    };
    no_availabilities?: string;
    loading?: string;
    error?: {
      no_email?: string;
      fetch_failed?: string;
    };
  };
  schedule?: {
    title?: string;
    loading?: string;
    error?: string;
    table?: {
      employee_id?: string;
      service_id?: string;
      customer_id?: string;
      start_time?: string;
      end_time?: string;
      status?: string;
      location?: string;
    };
  };
  
  
  
}

// Define context properties
export interface LanguageContextProps {
  language: Language;
  translations: Translations;
  toggleLanguage: () => void;
}

// ✅ Create the context (only export, no default value)
export const LanguageContext = createContext<LanguageContextProps | undefined>(undefined);
