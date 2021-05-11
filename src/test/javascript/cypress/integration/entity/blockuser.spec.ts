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

describe('Blockuser e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/blockusers*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('blockuser');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Blockusers', () => {
    cy.intercept('GET', '/api/blockusers*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blockuser');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Blockuser').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Blockuser page', () => {
    cy.intercept('GET', '/api/blockusers*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blockuser');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('blockuser');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Blockuser page', () => {
    cy.intercept('GET', '/api/blockusers*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blockuser');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Blockuser');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Blockuser page', () => {
    cy.intercept('GET', '/api/blockusers*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blockuser');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Blockuser');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Blockuser', () => {
    cy.intercept('GET', '/api/blockusers*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blockuser');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Blockuser');

    cy.get(`[data-cy="creationDate"]`).type('2021-05-10T12:46').invoke('val').should('equal', '2021-05-10T12:46');

    cy.setFieldSelectToLastOfEntity('blockeduser');

    cy.setFieldSelectToLastOfEntity('blockinguser');

    cy.setFieldSelectToLastOfEntity('cblockeduser');

    cy.setFieldSelectToLastOfEntity('cblockinguser');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/blockusers*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blockuser');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Blockuser', () => {
    cy.intercept('GET', '/api/blockusers*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/blockusers/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blockuser');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('blockuser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/blockusers*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('blockuser');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
