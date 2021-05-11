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

describe('Feedback e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/feedbacks*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('feedback');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Feedbacks', () => {
    cy.intercept('GET', '/api/feedbacks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Feedback').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Feedback page', () => {
    cy.intercept('GET', '/api/feedbacks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('feedback');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Feedback page', () => {
    cy.intercept('GET', '/api/feedbacks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Feedback');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Feedback page', () => {
    cy.intercept('GET', '/api/feedbacks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Feedback');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Feedback', () => {
    cy.intercept('GET', '/api/feedbacks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Feedback');

    cy.get(`[data-cy="creationDate"]`).type('2021-05-11T02:23').invoke('val').should('equal', '2021-05-11T02:23');

    cy.get(`[data-cy="name"]`).type('SMS', { force: true }).invoke('val').should('match', new RegExp('SMS'));

    cy.get(`[data-cy="email"]`)
      .type('Laurine.Stehr91@gmail.com', { force: true })
      .invoke('val')
      .should('match', new RegExp('Laurine.Stehr91@gmail.com'));

    cy.get(`[data-cy="feedback"]`)
      .type('Gorgeous Nicaragua back', { force: true })
      .invoke('val')
      .should('match', new RegExp('Gorgeous Nicaragua back'));

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/feedbacks*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Feedback', () => {
    cy.intercept('GET', '/api/feedbacks*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/feedbacks/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('feedback').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/feedbacks*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('feedback');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
