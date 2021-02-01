package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MonolithApp;
import com.mycompany.myapp.domain.Phone;
import com.mycompany.myapp.repository.PhoneRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.Os;
/**
 * Integration tests for the {@link PhoneResource} REST controller.
 */
@SpringBootTest(classes = MonolithApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PhoneResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    private static final Os DEFAULT_OS = Os.ANDROID;
    private static final Os UPDATED_OS = Os.IOS;

    private static final Boolean DEFAULT_PROMOTING = false;
    private static final Boolean UPDATED_PROMOTING = true;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhoneMockMvc;

    private Phone phone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phone createEntity(EntityManager em) {
        Phone phone = new Phone()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .os(DEFAULT_OS)
            .promoting(DEFAULT_PROMOTING);
        return phone;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phone createUpdatedEntity(EntityManager em) {
        Phone phone = new Phone()
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .os(UPDATED_OS)
            .promoting(UPDATED_PROMOTING);
        return phone;
    }

    @BeforeEach
    public void initTest() {
        phone = createEntity(em);
    }

    @Test
    @Transactional
    public void createPhone() throws Exception {
        int databaseSizeBeforeCreate = phoneRepository.findAll().size();
        // Create the Phone
        restPhoneMockMvc.perform(post("/api/phones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isCreated());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeCreate + 1);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPhone.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPhone.getOs()).isEqualTo(DEFAULT_OS);
        assertThat(testPhone.isPromoting()).isEqualTo(DEFAULT_PROMOTING);
    }

    @Test
    @Transactional
    public void createPhoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = phoneRepository.findAll().size();

        // Create the Phone with an existing ID
        phone.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneMockMvc.perform(post("/api/phones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = phoneRepository.findAll().size();
        // set the field null
        phone.setName(null);

        // Create the Phone, which fails.


        restPhoneMockMvc.perform(post("/api/phones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isBadRequest());

        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = phoneRepository.findAll().size();
        // set the field null
        phone.setPrice(null);

        // Create the Phone, which fails.


        restPhoneMockMvc.perform(post("/api/phones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isBadRequest());

        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPhones() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList
        restPhoneMockMvc.perform(get("/api/phones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phone.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].os").value(hasItem(DEFAULT_OS.toString())))
            .andExpect(jsonPath("$.[*].promoting").value(hasItem(DEFAULT_PROMOTING.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPhone() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get the phone
        restPhoneMockMvc.perform(get("/api/phones/{id}", phone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phone.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.os").value(DEFAULT_OS.toString()))
            .andExpect(jsonPath("$.promoting").value(DEFAULT_PROMOTING.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPhone() throws Exception {
        // Get the phone
        restPhoneMockMvc.perform(get("/api/phones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhone() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();

        // Update the phone
        Phone updatedPhone = phoneRepository.findById(phone.getId()).get();
        // Disconnect from session so that the updates on updatedPhone are not directly saved in db
        em.detach(updatedPhone);
        updatedPhone
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .os(UPDATED_OS)
            .promoting(UPDATED_PROMOTING);

        restPhoneMockMvc.perform(put("/api/phones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPhone)))
            .andExpect(status().isOk());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPhone.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPhone.getOs()).isEqualTo(UPDATED_OS);
        assertThat(testPhone.isPromoting()).isEqualTo(UPDATED_PROMOTING);
    }

    @Test
    @Transactional
    public void updateNonExistingPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneMockMvc.perform(put("/api/phones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePhone() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        int databaseSizeBeforeDelete = phoneRepository.findAll().size();

        // Delete the phone
        restPhoneMockMvc.perform(delete("/api/phones/{id}", phone.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
