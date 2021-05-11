import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Appuser e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/appusers*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('appuser');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Appusers', () => {
    cy.intercept('GET', '/api/appusers*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appuser');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Appuser').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Appuser page', () => {
    cy.intercept('GET', '/api/appusers*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appuser');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('appuser');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Appuser page', () => {
    cy.intercept('GET', '/api/appusers*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appuser');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Appuser');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Appuser page', () => {
    cy.intercept('GET', '/api/appusers*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appuser');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Appuser');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Appuser', () => {
    cy.intercept('GET', '/api/appusers*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appuser');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Appuser');

    cy.get(`[data-cy="creationDate"]`).type('2021-05-11T01:38').invoke('val').should('equal', '2021-05-11T01:38');


    cy.get(`[data-cy="bio"]`).type('Small systemic India', { force: true }).invoke('val').should('match', new RegExp('Small systemic India'));


    cy.get(`[data-cy="facebook"]`).type('collaborative multi-byte', { force: true }).invoke('val').should('match', new RegExp('collaborative multi-byte'));


    cy.get(`[data-cy="twitter"]`).type('generate Grass-roots Loan', { force: true }).invoke('val').should('match', new RegExp('generate Grass-roots Loan'));


    cy.get(`[data-cy="linkedin"]`).type('Connecticut Knolls generation', { force: true }).invoke('val').should('match', new RegExp('Connecticut Knolls generation'));


    cy.get(`[data-cy="instagram"]`).type('incubate Alaska Regional', { force: true }).invoke('val').should('match', new RegExp('incubate Alaska Regional'));


    cy.get(`[data-cy="birthdate"]`).type('2021-05-11T07:18').invoke('val').should('equal', '2021-05-11T07:18');

    cy.setFieldSelectToLastOfEntity('user');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/appusers*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appuser');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Appuser', () => {
    cy.intercept('GET', '/api/appusers*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/appusers/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appuser');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('appuser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/appusers*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('appuser');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
