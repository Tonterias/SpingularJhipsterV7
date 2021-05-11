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

describe('Community e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/communities*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('community');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Communities', () => {
    cy.intercept('GET', '/api/communities*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('community');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Community').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Community page', () => {
    cy.intercept('GET', '/api/communities*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('community');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('community');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Community page', () => {
    cy.intercept('GET', '/api/communities*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('community');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Community');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Community page', () => {
    cy.intercept('GET', '/api/communities*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('community');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Community');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Community', () => {
    cy.intercept('GET', '/api/communities*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('community');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Community');

    cy.get(`[data-cy="creationDate"]`).type('2021-05-10T14:46').invoke('val').should('equal', '2021-05-10T14:46');


    cy.get(`[data-cy="communityName"]`).type('Mississippi Secured SQL', { force: true }).invoke('val').should('match', new RegExp('Mississippi Secured SQL'));


    cy.get(`[data-cy="communityDescription"]`).type('Gloves Sleek Money', { force: true }).invoke('val').should('match', new RegExp('Gloves Sleek Money'));


    cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');


    cy.get(`[data-cy="isActive"]`).should('not.be.checked');
    cy.get(`[data-cy="isActive"]`).click().should('be.checked');
    cy.setFieldSelectToLastOfEntity('appuser');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/communities*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('community');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Community', () => {
    cy.intercept('GET', '/api/communities*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/communities/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('community');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('community').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/communities*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('community');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
